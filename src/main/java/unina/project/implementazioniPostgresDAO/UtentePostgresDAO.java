package unina.project.implementazioniPostgresDAO;

import unina.project.dao.UtenteDAO;
import unina.project.database.ConnessioneDatabase;
import unina.project.entity.Utente;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UtentePostgresDAO implements UtenteDAO {

    @Override
    public Utente login(String email, String password) {
        String query = "SELECT * FROM Utente WHERE email = ? AND password_utente = ?";
        Connection connessione = ConnessioneDatabase.getConnection();

        try (PreparedStatement stmt = connessione.prepareStatement(query)) {
            stmt.setString(1, email);
            stmt.setString(2, password);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Utente(
                            rs.getInt("id_utente"),
                            rs.getString("email"),
                            rs.getString("password_utente"),
                            rs.getString("nome"),
                            rs.getString("cognome")
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("Errore durante il login nel DAO: " + e.getMessage());
        }
        return null;
    }

    @Override
    public boolean registraUtente(String nome, String cognome, String email, String password) {
        String query = "INSERT INTO Utente (email, password_utente, nome, cognome) VALUES (?, ?, ?, ?)";
        Connection connessione = ConnessioneDatabase.getConnection();

        try (PreparedStatement stmt = connessione.prepareStatement(query)) {
            stmt.setString(1, email);
            stmt.setString(2, password);
            stmt.setString(3, nome);
            stmt.setString(4, cognome);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Errore durante la registrazione nel DAO: " + e.getMessage());
            return false;
        }
    }
}