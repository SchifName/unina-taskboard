package unina.project.controller;

import unina.project.database.ConnessioneDatabase;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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
}