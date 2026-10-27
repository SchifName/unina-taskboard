package unina.project.dao;

import unina.project.controller.RisultatoRegistrazione;
import unina.project.entity.Utente;

public interface UtenteDAO {
    Utente login(String email, String password);
    RisultatoRegistrazione registraUtente(String email, String password, String nome, String cognome);
    Utente getUtenteByEmail(String email);
}
