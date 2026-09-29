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
}
