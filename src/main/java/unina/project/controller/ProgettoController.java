package unina.project.controller;

import unina.project.dao.ProgettoDAO;
import unina.project.entity.Progetto;
import unina.project.entity.Utente;
import unina.project.implementazioniPostgresDAO.ProgettoPostgresDAO;

import java.util.ArrayList;
import java.util.List;

public class ProgettoController {

    private final AutenticazioneController authController;
    private final ProgettoDAO progettoDAO = new ProgettoPostgresDAO();
    private final List<Progetto> progettiUtente = new ArrayList<>();
    private Progetto progettoSelezionato;

    public ProgettoController(AutenticazioneController authController) {
        this.authController = authController;
    }

    public List<String> caricaNomiProgettiUtente() {
        progettiUtente.clear();
        List<String> nomiProgetti = new ArrayList<>();
        Utente u = authController.getUtenteCorrente();
        if (u == null) return nomiProgetti;

        List<Progetto> dalDB = progettoDAO.getProgettiByUtente(u.getIdUtente());
        progettiUtente.addAll(dalDB);

        for (Progetto p : progettiUtente) {
            nomiProgetti.add(p.toString());
        }
        return nomiProgetti;
    }

    public boolean selezionaProgetto(int indice) {
        if (indice >= 0 && indice < progettiUtente.size()) {
            this.progettoSelezionato = progettiUtente.get(indice);
            return true;
        }
        return false;
    }

    public Progetto getProgettoSelezionato() {
        return progettoSelezionato;
    }

    public String getNomeProgettoSelezionato() {
        return progettoSelezionato != null ? progettoSelezionato.getNome() : "";
    }

    public boolean creaNuovoProgetto(String nome, String descrizione) {
        Utente u = authController.getUtenteCorrente();
        if (u == null || nome == null || nome.isBlank()) return false;
        return progettoDAO.creaProgetto(nome.trim(), descrizione != null ? descrizione.trim() : "", u.getIdUtente());
    }

    public boolean eliminaProgetto(int indice) {
        if (indice < 0 || indice >= progettiUtente.size()) return false;
        progettiUtente.remove(indice);
        return true;
    }

    public String generaReportGlobale() {
        caricaNomiProgettiUtente();
        StringBuilder reportText = new StringBuilder();
        for (Progetto p : progettiUtente) {
            reportText.append("📁 Progetto: ").append(p.getNome()).append("\n\n");
        }
        return reportText.toString();
    }
}