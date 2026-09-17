package unina.project.gui;
/**
 * Controller EBC per la logica di autenticazione e registrazione.
 */

public class AutenticazioneController {
    public boolean effettuaLogin(String email, String password) {
        // TODO: Inserire qui la query JDBC verso il database (es. SELECT * FROM Utente WHERE email = ? AND password = ?)
        // Per test in VS Code, restituiamo true se i campi non sono vuoti
        return email != null && !email.isEmpty() && password != null && !password.isEmpty();
    }

    public boolean registraUtente(String nome, String email, String password) {
        // TODO: Inserire qui l'istruzione SQL di inserimento (INSERT INTO Utente...)
        return nome != null && !nome.isEmpty() && email.contains("@unina.it");
    }
}


}
