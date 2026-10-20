package unina.project.dao;

import unina.project.entity.Utente;

public interface UtenteDAO {
    Utente login(String email, String password);
    boolean registraUtente(String email, String password, String nome, String cognome);
}
