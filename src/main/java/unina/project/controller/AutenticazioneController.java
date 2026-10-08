package unina.project.controller; // Cambialo in unina.project.control se hai creato il package

import unina.project.database.ConnessioneDatabase;
import unina.project.entity.Utente;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AutenticazioneController {

    // Questo metodo interroga il DB. Se le credenziali sono corrette,
    // restituisce un oggetto Utente, altrimenti restituisce null.
    public Utente effettuaLogin(String email, String password) {
        String query = "SELECT * FROM Utente WHERE email = ? AND password_utente = ?";
        Connection connessione = ConnessioneDatabase.getConnection();

        // Usiamo PreparedStatement per evitare attacchi SQL Injection
        try (PreparedStatement stmt = connessione.prepareStatement(query)) {

            // Sostituiamo i "?" nella query con i parametri inseriti dall'utente
            stmt.setString(1, email);
            stmt.setString(2, password);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    // Trovato! Estraiamo i dati dalla riga del database e creiamo l'Entity
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
            System.err.println("Errore durante il login: " + e.getMessage());
        }

        // Se arriviamo qui, l'utente non esiste o la password è sbagliata
        return null;
    }
}