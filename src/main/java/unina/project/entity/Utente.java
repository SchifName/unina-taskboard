package unina.project.entity;

public class Utente {
    private int idUtente;
    private String email;
    private String password;
    private String nome;
    private String cognome;

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

    // Setters
    public void setIdUtente(int idUtente) { this.idUtente = idUtente; }
    public void setEmail(String email) { this.email = email; }
    public void setPassword(String password) { this.password = password; }
    public void setNome(String nome) { this.nome = nome; }
    public void setCognome(String cognome) { this.cognome = cognome; }

    // Utile per visualizzare rapidamente il nome completo nella GUI
    public String getNomeCompleto() {
        return nome + " " + cognome;
    }
}
