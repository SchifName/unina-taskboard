package unina.project.controller;

import unina.project.dao.UtenteDAO;
import unina.project.entity.Utente;
import unina.project.implementazioniPostgresDAO.UtentePostgresDAO;

public class AutenticazioneController {

    private final UtenteDAO utenteDAO = new UtentePostgresDAO();
    private Utente utenteCorrente;

    public boolean effettuaLogin(String email, String password) {
        this.utenteCorrente = utenteDAO.login(email, password);
        return this.utenteCorrente != null;
    }

    public RisultatoRegistrazione registraUtente(String email, String password, String nome, String cognome) {
        return utenteDAO.registraUtente(email, password, nome, cognome);
    }

    public void effettuaLogout() {
        this.utenteCorrente = null;
    }

    public Utente getUtenteCorrente() {
        return utenteCorrente;
    }

    public String getNomeCompletoUtente() {
        return utenteCorrente != null ? utenteCorrente.getNomeCompleto() : "";
    }

    public String getEmailUtente() {
        return utenteCorrente != null ? utenteCorrente.getEmail() : "";
    }

}