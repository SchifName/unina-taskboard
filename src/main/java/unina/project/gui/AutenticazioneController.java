package unina.project.gui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AutenticazioneController {

    // Database temporaneo in memoria per far funzionare tutto subito senza errori
    private static final Map<String, String> utentiPassword = new HashMap<>();
    private static final Map<String, String[]> utentiInfo = new HashMap<>();
    private static final List<String> progettiMemoria = new ArrayList<>(List.of("Progetto Esame OOP", "Sviluppo TaskBoard"));

    public boolean effettuaLogin(String email, String password) {
        if (email == null || password == null) return false;

        email = email.trim();

        // Se l'utente è stato appena registrato in questa sessione
        if (utentiPassword.containsKey(email) && utentiPassword.get(email).equals(password)) {
            return true;
        }

        // Accesso libero di test per qualsiasi email @unina.it se non ti sei ancora registrato
        return email.endsWith("@unina.it") && !password.isEmpty();
    }

    public boolean registraUtente(String nome, String email, String password, String ruolo) {
        if (nome != null && !nome.trim().isEmpty() && email != null && email.contains("@unina.it") && password != null && !password.isEmpty()) {
            email = email.trim();
            utentiPassword.put(email, password);
            utentiInfo.put(email, new String[]{nome.trim(), ruolo});
            return true;
        }
        return false;
    }

    public String[] getInfoUtente(String email) {
        if (email != null && utentiInfo.containsKey(email.trim())) {
            return utentiInfo.get(email.trim());
        }
        // Dati di default se fai il login rapido di test
        return new String[] { "Mario Rossi", "Studente" };
    }

    public List<String> getNomiProgettiUtente() {
        return progettiMemoria;
    }

    public void creaNuovoProgetto(String nome) {
        if (nome != null && !nome.trim().isEmpty()) {
            progettiMemoria.add(nome.trim());
        }
    }
}