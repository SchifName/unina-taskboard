package unina.project.implementazioniPostgresDAO;

import unina.project.dao.FileDAO;
import unina.project.database.ConnessioneDatabase;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FilePostgresDAO implements FileDAO {

    @Override
    public boolean salvaFileERevisione(int idTask, int idAutore, String nome, String estensione, byte[] contenuto, String notaDescrittiva) {
        String insertFile = "INSERT INTO File_task (id_task, nome_file, estensione) VALUES (?, ?, ?) RETURNING id_file";
        String insertRev = "INSERT INTO Revisione_file (id_file_rev, id_autore, nota_descrittiva, contenuto) VALUES (?, ?, ?, ?)";

        Connection connessione = ConnessioneDatabase.getConnection();

        try {
            connessione.setAutoCommit(false);
            int idFileGenerato = -1;

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

            try (PreparedStatement stmtRev = connessione.prepareStatement(insertRev)) {
                stmtRev.setInt(1, idFileGenerato);
                stmtRev.setInt(2, idAutore);
                stmtRev.setString(3, notaDescrittiva);
                stmtRev.setBytes(4, contenuto);
                stmtRev.executeUpdate();
            }

            connessione.commit();
            return true;

        } catch (SQLException e) {
            try { if (connessione != null) connessione.rollback(); } catch (SQLException ex) {}
            System.err.println("Errore salvataggio file nel DAO: " + e.getMessage());
            return false;
        } finally {
            try { if (connessione != null) connessione.setAutoCommit(true); } catch (SQLException ex) {}
        }
    }

    @Override
    public byte[] getContenutoUltimaRevisione(int idTask) {
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
                    return rs.getBytes("contenuto");
                }
            }
        } catch (SQLException e) {
            System.err.println("Errore recupero contenuto revisione nel DAO: " + e.getMessage());
        }
        return null;
    }

    @Override
    public String getNomeFileOriginale(int idTask) {
        String query = "SELECT nome_file, estensione FROM File_task WHERE id_task = ?";
        Connection conn = ConnessioneDatabase.getConnection();
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, idTask);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String nome = rs.getString("nome_file");
                    String ext = rs.getString("estensione");
                    return ext.isEmpty() ? nome : nome + "." + ext;
                }
            }
        } catch (SQLException e) {
            System.err.println("Errore lettura nome file nel DAO: " + e.getMessage());
        }
        return "Download_Task";
    }

    @Override
    public List<String> getStoricoRevisioni(int idTask) {
        List<String> storico = new ArrayList<>();
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
                    String dataStr = rs.getString("data_modifica");
                    String autore = rs.getString("nome") + " " + rs.getString("cognome");

                    if (dataStr != null && dataStr.contains(".")) {
                        dataStr = dataStr.substring(0, dataStr.lastIndexOf('.'));
                    }

                    String riga = "Versione " + versione + " " + dataStr + "\n" +
                            "Autore: " + autore + "\n" +
                            "Nota: " + nota;

                    storico.add(riga);
                }
            }
        } catch (SQLException e) {
            System.err.println("Errore caricamento storico nel DAO: " + e.getMessage());
        }
        return storico;
    }
}