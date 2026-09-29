package unina.project.entity;

public class Progetto {
    private int idProgetto;
    private String nome;
    private String descrizione;
    private int idCreatore;

    public Progetto(int idProgetto, String nome, String descrizione, int idCreatore) {
        this.idProgetto = idProgetto;
        this.nome = nome;
        this.descrizione = descrizione;
        this.idCreatore = idCreatore;
    }

    public int getIdProgetto() { return idProgetto; }
    public String getNome() { return nome; }
    public String getDescrizione() { return descrizione; }
    public int getIdCreatore() { return idCreatore; }

    public void setIdProgetto(int idProgetto) { this.idProgetto = idProgetto; }
    public void setNome(String nome) { this.nome = nome; }
    public void setDescrizione(String descrizione) { this.descrizione = descrizione; }
    public void setIdCreatore(int idCreatore) { this.idCreatore = idCreatore; }

    @Override
    public String toString() {
        // Quando inserirai l'oggetto Progetto in una ListView JavaFX,
        // verrà mostrato automaticamente il suo nome grazie a questo metodo.
        return nome;
    }
}