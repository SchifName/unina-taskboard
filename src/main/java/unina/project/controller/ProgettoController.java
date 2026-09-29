package unina.project.controller;

import unina.project.database.ConnessioneDatabase;
import unina.project.entity.Progetto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProgettoController {

    public List<Progetto> getProgettiUtente(int idUtente) {
        List<Progetto> progetti = new ArrayList<>();


        String query = "SELECT p.* FROM Progetto p " +
                "JOIN Membro_progetto mp ON p.id_progetto = mp.id_prog " +
                "WHERE mp.id_membro = ?";

        Connection conn = ConnessioneDatabase.getConnection();
        try (PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, idUtente);// questo serve a dare un valore al ? nella query

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    // Crea un oggetto Entity per ogni riga trovata nel DB
                    Progetto p = new Progetto(
                            rs.getInt("id_progetto"),
                            rs.getString("nome"),
                            rs.getString("descrizione"),
                            rs.getInt("id_creatore")
                    );
                    progetti.add(p);
                }
            }
        } catch (SQLException e) {
            System.err.println("Errore nel caricamento progetti: " + e.getMessage());
        }

        return progetti;
    }
}