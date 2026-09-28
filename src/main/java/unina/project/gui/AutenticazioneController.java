package unina.project.gui;

import java.util.HashMap;
import java.util.Map;

public class AutenticazioneController {

    // Database simulato in memoria con più utenti
    private static final Map<String, String[]> utentiDB = new HashMap<>();

    static {
        // Formato: "email" -> { "Nome Cognome", "Ruolo" }
        utentiDB.put("mario.rossi@studenti.unina.it", new String[]{"Mario Rossi", "Studente"});
        utentiDB.put("luigi.bianchi@studenti.unina.it", new String[]{"Luigi Bianchi", "Studente"});
        utentiDB.put("anna.verdi@unina.it", new String[]{"Prof. Anna Verdi", "Docente"});
        utentiDB.put("giulia.neri@studenti.unina.it", new String[]{"Giulia Neri", "Studente"});
    }

    public boolean effettuaLogin(String email, String password) {
        return email != null && utentiDB.containsKey(email) && password != null && password.length() >= 4;
    }

    public String[] getInfoUtente(String email) {
        return utentiDB.getOrDefault(email, new String[]{"Utente Ospite", "N/D"});
    }
}