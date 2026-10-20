package unina.project.dao;

import unina.project.entity.Attivita;
import java.time.LocalDate;
import java.util.List;

public interface AttivitaDAO {
    List<Attivita> getAttivitaByProgetto(int idProgetto);
    boolean inserisciAttivita(int idProgetto, String titolo, String descrizione, LocalDate scadenza, String tipo, String stato, int idCreatore);
}
