package unina.project.entity;

public class Utente {
    private int idUtente;
    private String email;
    private String password;
    private String nome;
    private String cognome;
    private String ruolo; // Aggiunto l'attributo per memorizzare il ruolo dell'utente (es. Studente o Docente)

    public Utente(int idUtente, String email, String password, String nome, String cognome) {
        this.idUtente = idUtente;
        this.email = email;
        this.password = password;
        this.nome = nome;
        this.cognome = cognome;
    }

    // Getters
    public int getIdUtente() { return idUtente; }
    public String getEmail() { return email; }
    public String getPassword() { return password; }
    public String getNome() { return nome; }
    public String getCognome() { return cognome; }
    public String getRuolo() { return ruolo; } // Restituisce il valore del ruolo dell'utente

    // Setters
    public void setIdUtente(int idUtente) { this.idUtente = idUtente; }
    public void setEmail(String email) { this.email = email; }
    public void setPassword(String password) { this.password = password; }
    public void setNome(String nome) { this.nome = nome; }
    public void setCognome(String cognome) { this.cognome = cognome; }
    public void setRuolo(String ruolo) { this.ruolo = ruolo; } // Imposta il valore del ruolo dell'utente

    // Utile per visualizzare rapidamente il nome completo nella GUI
    public String getNomeCompleto() {
        return nome + " " + cognome;
    }
}