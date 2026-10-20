package unina.project.controller;

import unina.project.dao.AttivitaDAO;
import unina.project.entity.Attivita;
import unina.project.entity.Progetto;
import unina.project.entity.Utente;
import unina.project.implementazioniPostgresDAO.AttivitaPostgresDAO;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class AttivitaController {

    private final AutenticazioneController authController;
    private final ProgettoController progettoController;
    private final AttivitaDAO attivitaDAO = new AttivitaPostgresDAO();
    private final List<Attivita> listaAttivita = new ArrayList<>();
    private Attivita attivitaSelezionata;

    public AttivitaController(AutenticazioneController authController, ProgettoController progettoController) {
        this.authController = authController;
        this.progettoController = progettoController;
    }

    public List<String> caricaAttivitaProgettoCorrente() {
        listaAttivita.clear();
        List<String> elencoTestuale = new ArrayList<>();
        Progetto p = progettoController.getProgettoSelezionato();
        if (p == null) return elencoTestuale;

        List<Attivita> dalDB = attivitaDAO.getAttivitaByProgetto(p.getIdProgetto());
        listaAttivita.addAll(dalDB);

        for (Attivita a : listaAttivita) {
            elencoTestuale.add(a.toString());
        }
        return elencoTestuale;
    }

    public boolean selezionaAttivita(int indice) {
        if (indice >= 0 && indice < listaAttivita.size()) {
            this.attivitaSelezionata = listaAttivita.get(indice);
            return true;
        }
        this.attivitaSelezionata = null;
        return false;
    }

    public Attivita getAttivitaSelezionata() {
        return attivitaSelezionata;
    }

    public String getTitoloAttivitaSelezionata() {
        return attivitaSelezionata != null ? attivitaSelezionata.getTitolo() : "";
    }

    public boolean inserisciAttivita(String titolo, String descrizione, LocalDate scadenza, String tipo, String statoUI) {
        Progetto p = progettoController.getProgettoSelezionato();
        Utente u = authController.getUtenteCorrente();
        if (p == null || u == null || titolo == null || titolo.isBlank()) return false;

        String statoDB = "(Completata)".equals(statoUI) ? "Finita"
                : "(In Corso)".equals(statoUI) ? "Presa_in_carico" : "Nuova";

        return attivitaDAO.inserisciAttivita(
                p.getIdProgetto(),
                titolo.trim(),
                descrizione != null ? descrizione.trim() : "",
                scadenza,
                tipo.trim(),
                statoDB,
                u.getIdUtente()
        );
    }

    public void aggiornaDatiStatistiche() {
        caricaAttivitaProgettoCorrente();
    }

    public int getNumeroCompletate() {
        int count = 0;
        for (Attivita a : listaAttivita) {
            if ("Finita".equals(a.getStato())) count++;
        }
        return count;
    }

    public int getNumeroInCorso() {
        int count = 0;
        for (Attivita a : listaAttivita) {
            if ("Presa_in_carico".equals(a.getStato())) count++;
        }
        return count;
    }

    public int getNumeroNuove() {
        int count = 0;
        for (Attivita a : listaAttivita) {
            if (!"Finita".equals(a.getStato()) && !"Presa_in_carico".equals(a.getStato())) count++;
        }
        return count;
    }

    public String getTestoDettaglioStatistiche() {
        int c = 0, ic = 0, ni = 0;
        StringBuilder completateNomi = new StringBuilder();
        StringBuilder inCorsoNomi = new StringBuilder();
        StringBuilder nonIniziateNomi = new StringBuilder();

        for (Attivita task : listaAttivita) {
            if ("Finita".equals(task.getStato())) {
                c++;
                completateNomi.append("• ").append(task).append("\n");
            } else if ("Presa_in_carico".equals(task.getStato())) {
                ic++;
                inCorsoNomi.append("• ").append(task).append("\n");
            } else {
                ni++;
                nonIniziateNomi.append("• ").append(task).append("\n");
            }
        }

        return "✅ COMPLETATE (" + c + "):\n" + (completateNomi.length() > 0 ? completateNomi : "Nessuna\n") + "\n\n" +
                "⏳ IN CORSO (" + ic + "):\n" + (inCorsoNomi.length() > 0 ? inCorsoNomi : "Nessuna\n") + "\n\n" +
                "❌ NUOVE (" + ni + "):\n" + (nonIniziateNomi.length() > 0 ? nonIniziateNomi : "Nessuna\n");
    }
}