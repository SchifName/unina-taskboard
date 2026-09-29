package unina.project.controller;
import unina.project.database.ConnessioneDatabase;
import unina.project.entity.Attivita;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AttivitaController {
    public List<Attivita> getAttivitaByProgetto(int idProgetto) {
        List<Attivita> listaAttivita = new ArrayList<>();
        String query = "SELECT * FROM Attivita WHERE id_prog = ?";

        Connection connessione = ConnessioneDatabase.getConnection();
        try (PreparedStatement stmt = connessione.prepareStatement(query)){
            stmt.setInt(1, idProgetto);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Attivita a = new Attivita(
                            rs.getInt("id_attivita"),
                            rs.getInt("id_prog"),
                            rs.getString("titolo"),
                            rs.getString("descrizione"),
                            rs.getDate("scadenza") != null ? rs.getDate("scadenza").toLocalDate() : null,
                            rs.getString("tipo"),
                            rs.getString("stato"),
                            rs.getInt("id_creatore")
                    );
                    listaAttivita.add(a);
                }
            }
        }catch (SQLException e){
            System.err.println("Errore caricamento Attivita");
        }
        return listaAttivita;
    }
    public boolean inserisciAttivita(int idProgetto, String titolo, String stato, int idCreatore) {
        String query = "INSERT INTO Attivita (id_prog, titolo, stato, id_creatore) VALUES (?, ?, ?, ?)";

        try (Connection conn = ConnessioneDatabase.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, idProgetto);
            stmt.setString(2, titolo);
            stmt.setString(3, stato);
            stmt.setInt(4, idCreatore);

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Errore inserimento attività: " + e.getMessage());
            return false;
        }
    }
}
