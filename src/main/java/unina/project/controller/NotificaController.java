package unina.project.controller;

import unina.project.entity.Attivita;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class NotificaController {

    // Metodo che simula o recupera le attività in scadenza imminente (es. entro 2 giorni)
    public List<String> controllaScadenzeImminenti(int idUtente, List<Attivita> attivitaUtente) {
        List<String> messaggiNotifica = new ArrayList<>();
        LocalDate oggi = LocalDate.now();
        LocalDate limiteScadenza = oggi.plusDays(2); // Entro 2 giorni

        for (Attivita a : attivitaUtente) {
            if (a.getScadenza() != null && !a.getStato().equals("Finita")) {
                if (!a.getScadenza().isBefore(oggi) && !a.getScadenza().isAfter(limiteScadenza)) {
                    messaggiNotifica.add("⚠️ Scadenza imminente per l'attività: \"" + a.getTitolo() + "\" (Scadenza: " + a.getScadenza() + ")");
                }
            }
        }
        return messaggiNotifica;
    }
}