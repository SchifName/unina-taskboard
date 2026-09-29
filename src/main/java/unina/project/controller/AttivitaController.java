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
    public boolean inserisciAttivita(int idProgetto, String titolo, String descrizione, java.time.LocalDate scadenza, String tipo, String stato, int idCreatore) {
        // Guarda il quinto e il sesto punto interrogativo: abbiamo aggiunto ::tipo_attivita e ::stato_attivita
        String query = "INSERT INTO Attivita (id_prog, titolo, descrizione, scadenza, tipo, stato, id_creatore) VALUES (?, ?, ?, ?, ?::tipo_attivita, ?::stato_attivita, ?)";

        Connection conn = ConnessioneDatabase.getConnection();
        try (PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, idProgetto);
            stmt.setString(2, titolo);
            stmt.setString(3, descrizione);

            if (scadenza != null) {
                stmt.setDate(4, java.sql.Date.valueOf(scadenza));
            } else {
                stmt.setNull(4, java.sql.Types.DATE);
            }

            stmt.setString(5, tipo);
            stmt.setString(6, stato);
            stmt.setInt(7, idCreatore);

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Errore inserimento attività completa: " + e.getMessage());
            return false;
        }
    }
}
