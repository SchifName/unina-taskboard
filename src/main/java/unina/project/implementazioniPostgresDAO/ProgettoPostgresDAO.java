package unina.project.implementazioniPostgresDAO;

import unina.project.dao.ProgettoDAO;
import unina.project.database.ConnessioneDatabase;
import unina.project.entity.Progetto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProgettoPostgresDAO implements ProgettoDAO {

    @Override
    public List<Progetto> getProgettiByUtente(int idUtente) {
        List<Progetto> progetti = new ArrayList<>();
        String query = "SELECT p.* FROM Progetto p " +
                "JOIN Membro_progetto mp ON p.id_progetto = mp.id_prog " +
                "WHERE mp.id_membro = ?";

        Connection conn = ConnessioneDatabase.getConnection();
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, idUtente);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
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
            System.err.println("Errore nel caricamento progetti nel DAO: " + e.getMessage());
        }
        return progetti;
    }

    @Override
    public boolean creaProgetto(String nome, String descrizione, int idCreatore) {
        String insertProg = "INSERT INTO Progetto (nome, descrizione, id_creatore) VALUES (?, ?, ?) RETURNING id_progetto";
        String insertMembro = "INSERT INTO Membro_progetto (id_prog, id_membro) VALUES (?, ?)";

        Connection conn = ConnessioneDatabase.getConnection();
        try {
            conn.setAutoCommit(false);
            int idNuovoProg = -1;

            try (PreparedStatement stmt = conn.prepareStatement(insertProg)) {
                stmt.setString(1, nome);
                stmt.setString(2, descrizione);
                stmt.setInt(3, idCreatore);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        idNuovoProg = rs.getInt(1);
                    }
                }
            }

            if (idNuovoProg != -1) {
                try (PreparedStatement stmtM = conn.prepareStatement(insertMembro)) {
                    stmtM.setInt(1, idNuovoProg);
                    stmtM.setInt(2, idCreatore);
                    stmtM.executeUpdate();
                }
                conn.commit();
                return true;
            }
            conn.rollback();
        } catch (SQLException e) {
            try { if (conn != null) conn.rollback(); } catch (SQLException ex) {}
            System.err.println("Errore creazione progetto nel DAO: " + e.getMessage());
        } finally {
            try { if (conn != null) conn.setAutoCommit(true); } catch (SQLException ex) {}
        }
        return false;
    }
}