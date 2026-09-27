package unina.project.gui;

import java.util.ArrayList;
import java.util.List;

/**
 * Controller EBC per la gestione della logica di autenticazione, registrazione
 * e dei progetti all'interno della piattaforma UninaTaskBoard.
 */
public class AutenticazioneController {

    // Lista temporanea in memoria per testare i progetti senza database per ora
    private static List<String> progettiMemoria = new ArrayList<>(List.of("Progetto Esame SINF", "Sviluppo TaskBoard"));

    /**
     * Gestisce la logica di verifica delle credenziali per il login.
     */
    public boolean effettuaLogin(String email, String password) {
        if (email == null || email.trim().isEmpty() || password == null || password.trim().isEmpty()) {
            return false;
        }

        // TODO: Inserire qui la query JDBC verso il database (es. SELECT * FROM Utente WHERE email = ? AND password = ?)
        boolean isEmailValida = email.contains("@unina.it") || email.contains("@");
        return isEmailValida && password.length() >= 4;
    }

    /**
     * Gestisce la logica di registrazione di un nuovo utente nel sistema.
     */
    public boolean registraUtente(String nome, String email, String password) {
        if (nome == null || nome.trim().isEmpty()) {
            return false;
        }
        if (email == null || !email.contains("@unina.it")) {
            return false;
        }
        if (password == null || password.length() < 6) {
            return false;
        }

        // TODO: Inserire qui l'istruzione SQL di inserimento (INSERT INTO Utente...)
        return true;
    }

    /**
     * Restituisce l'elenco dei progetti associati all'utente.
     */
    public List<String> getNomiProgettiUtente() {
        // TODO: In seguito qui inserisci la query JDBC per leggerli da PostgreSQL
        return progettiMemoria;
    }

    /**
     * Crea un nuovo progetto aggiungendolo al sistema.
     */
    public void creaNuovoProgetto(String nome) {
        if (nome != null && !nome.trim().isEmpty()) {
            progettiMemoria.add(nome);
            // TODO: In seguito qui inserisci la INSERT SQL su PostgreSQL
        }
    }
}