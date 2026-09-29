package unina.project.controller;

import unina.project.database.ConnessioneDatabase;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;

public class FileController {

    public boolean salvaFileERevisione(int idTask, int idAutore, File fileDaCaricare, String notaDescrittiva) {
        // Le due query: la prima usa RETURNING per darci subito l'ID appena creato
        String insertFile = "INSERT INTO File_task (id_task, nome_file, estensione) VALUES (?, ?, ?) RETURNING id_file";
        String insertRev = "INSERT INTO Revisione_file (id_file_rev, id_autore, nota_descrittiva, contenuto) VALUES (?, ?, ?, ?)";

        Connection connessione = ConnessioneDatabase.getConnection();

        try {
            // Disabilitiamo l'autocommit: se fallisce una delle due query, annulla tutto
            connessione.setAutoCommit(false);

            // Estrapoliamo il nome e l'estensione dal file selezionato
            String nomeCompleto = fileDaCaricare.getName();
            String nome = nomeCompleto.contains(".") ? nomeCompleto.substring(0, nomeCompleto.lastIndexOf('.')) : nomeCompleto;
            String estensione = nomeCompleto.contains(".") ? nomeCompleto.substring(nomeCompleto.lastIndexOf('.') + 1) : "";

            int idFileGenerato = -1;

            // Inseriamo il file principale e recuperiamo il suo ID
            try (PreparedStatement stmtFile = connessione.prepareStatement(insertFile)) {
                stmtFile.setInt(1, idTask);
                stmtFile.setString(2, nome);
                stmtFile.setString(3, estensione);

                try (ResultSet rs = stmtFile.executeQuery()) {
                    if (rs.next()) {
                        idFileGenerato = rs.getInt(1);
                    }
                }
            }

            if (idFileGenerato == -1) {
                connessione.rollback();
                return false;
            }

            // Convertiamo il file fisico in un array di byte!
            byte[] fileInByte = Files.readAllBytes(fileDaCaricare.toPath());

            // Inseriamo la prima revisione con i byte del file
            try (PreparedStatement stmtRev = connessione.prepareStatement(insertRev)) {
                stmtRev.setInt(1, idFileGenerato);
                stmtRev.setInt(2, idAutore);
                stmtRev.setString(3, notaDescrittiva);
                stmtRev.setBytes(4, fileInByte); // Metodo specifico per i BYTEA
                stmtRev.executeUpdate();
            }

            // Se è andato tutto bene, confermiamo il salvataggio nel DB
            connessione.commit();
            return true;

        } catch (SQLException | IOException e) {
            try { if (connessione != null) connessione.rollback(); } catch (SQLException ex) {}
            System.err.println("Errore salvataggio file : " +e.getMessage());
            return false;
        } finally {
            try { if (connessione != null) connessione.setAutoCommit(true); } catch (SQLException ex) {}
        }
    }

    public boolean scaricaUltimaRevisione(int idTask, File fileDestinazione) {
        // Cerchiamo il file collegato alla task e prendiamo l'ultima revisione (ordinando in modo decrescente)
        String query = "SELECT r.contenuto " +
                "FROM File_task f " +
                "JOIN Revisione_file r ON f.id_file = r.id_file_rev " +
                "WHERE f.id_task = ? " +
                "ORDER BY r.versione DESC LIMIT 1";

        Connection connessione = ConnessioneDatabase.getConnection();

        try (PreparedStatement stmt = connessione.prepareStatement(query)) {
            stmt.setInt(1, idTask);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    // Estraiamo l'array di byte da PostgreSQL
                    byte[] fileInByte = rs.getBytes("contenuto");

                    // Scriviamo i byte nel percorso scelto dall'utente sul suo PC
                    Files.write(fileDestinazione.toPath(), fileInByte);
                    return true;
                }
            }
        } catch (SQLException | IOException e) {
            System.err.println("Errore durante il download del file: " + e.getMessage());
        }
        return false;
    }

    public String getNomeFileOriginale(int idTask) {
        String query = "SELECT nome_file, estensione FROM File_task WHERE id_task = ?";
        Connection conn = ConnessioneDatabase.getConnection();
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, idTask);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String nome = rs.getString("nome_file");
                    String ext = rs.getString("estensione");
                    // Ricostruisce il nome originale (es. "documento.txt")
                    return ext.isEmpty() ? nome : nome + "." + ext;
                }
            }
        } catch (SQLException e) {
            System.err.println("Errore lettura nome file: " + e.getMessage());
        }
        return "Download_Task_" + idTask; // Nome di emergenza
    }

    public List<String> getStoricoRevisioni(int idTask) {
        List<String> storico = new ArrayList<>();

        // Mettiamo in JOIN file, revisioni e utente per avere nome e cognome dell'autore
        String query = "SELECT r.versione, r.nota_descrittiva, r.data_modifica, u.nome, u.cognome " +
                "FROM File_task f " +
                "JOIN Revisione_file r ON f.id_file = r.id_file_rev " +
                "JOIN Utente u ON r.id_autore = u.id_utente " +
                "WHERE f.id_task = ? " +
                "ORDER BY r.versione DESC";

        Connection conn = ConnessioneDatabase.getConnection();
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, idTask);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    int versione = rs.getInt("versione");
                    String nota = rs.getString("nota_descrittiva");
                    String dataStr = rs.getString("data_modifica"); // Lo prendiamo come stringa per semplicità
                    String autore = rs.getString("nome") + " " + rs.getString("cognome");

                    // Tronchiamo un po' la data se è troppo lunga (es. togliamo i millisecondi)
                    if (dataStr != null && dataStr.contains(".")) {
                        dataStr = dataStr.substring(0, dataStr.lastIndexOf('.'));
                    }

                    // Assembliamo la riga per l'interfaccia grafica
                    String riga = "Versione " + versione + " " + dataStr + "\n" +
                            "Autore: " + autore + "\n" +
                            "Nota: " + nota;

                    storico.add(riga);
                }
            }
        } catch (SQLException e) {
            System.err.println("Errore caricamento storico: " + e.getMessage());
        }
        return storico;
    }

}