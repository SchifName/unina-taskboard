package unina.project.implementazioniPostgresDAO;

import unina.project.controller.RisultatoRegistrazione;
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
    public RisultatoRegistrazione registraUtente(String email, String password, String nome, String cognome) {
        String query = "INSERT INTO Utente (email, password_utente, nome, cognome) VALUES (?, ?, ?, ?)";
        Connection connessione = ConnessioneDatabase.getConnection();

        try (PreparedStatement stmt = connessione.prepareStatement(query)) {
            stmt.setString(1, email);
            stmt.setString(2, password);
            stmt.setString(3, nome);
            stmt.setString(4, cognome);
            if (stmt.executeUpdate() > 0) {
                return RisultatoRegistrazione.SUCCESSO;
            }else {
                return RisultatoRegistrazione.ERR_GENERICO;
            }
        } catch (SQLException e) {
            if ("23505".equals(e.getSQLState())){
                System.err.println("Errore durante la registrazione nel DAO: " + e.getMessage());
                return RisultatoRegistrazione.ERR_EMAIL;
            }
            System.err.println("Errore durante la registrazione nel DAO: " + e.getMessage());
            return RisultatoRegistrazione.ERR_GENERICO;
        }
    }

    @Override
    public Utente getUtenteByEmail(String email) {
        String query = "SELECT * FROM Utente WHERE email = ?";
        Connection connessione = ConnessioneDatabase.getConnection();

        try (PreparedStatement stmt = connessione.prepareStatement(query)) {
            stmt.setString(1, email);
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
            System.err.println("Errore ricerca utente per email nel DAO: " + e.getMessage());
        }
        return null;
    }
}