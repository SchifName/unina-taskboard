package unina.project.entity;

import java.time.LocalDate;

public class Attivita {
    private int idAttivita;
    private int idProg;
    private String titolo;
    private String descrizione;
    private LocalDate scadenza;
    private String tipo; // "Sviluppo", "Studio", "Documentazione"
    private String stato; // "Nuova", "Presa_in_carico", "Finita"
    private int idCreatore;

    public Attivita(int idAttivita, int idProg, String titolo, String descrizione, LocalDate scadenza, String tipo, String stato, int idCreatore) {
        this.idAttivita = idAttivita;
        this.idProg = idProg;
        this.titolo = titolo;
        this.descrizione = descrizione;
        this.scadenza = scadenza;
        this.tipo = tipo;
        this.stato = stato;
        this.idCreatore = idCreatore;
    }

    // Getters
    public int getIdAttivita() { return idAttivita; }
    public int getIdProg() { return idProg; }
    public String getTitolo() { return titolo; }
    public String getDescrizione() { return descrizione; }
    public LocalDate getScadenza() { return scadenza; }
    public String getTipo() { return tipo; }
    public String getStato() { return stato; }
    public int getIdCreatore() { return idCreatore; }

    // Setters
    public void setIdAttivita(int idAttivita) { this.idAttivita = idAttivita; }
    public void setIdProg(int idProg) { this.idProg = idProg; }
    public void setTitolo(String titolo) { this.titolo = titolo; }
    public void setDescrizione(String descrizione) { this.descrizione = descrizione; }
    public void setScadenza(LocalDate scadenza) { this.scadenza = scadenza; }
    public void setTipo(String tipo) { this.tipo = tipo; }
    public void setStato(String stato) { this.stato = stato; }
    public void setIdCreatore(int idCreatore) { this.idCreatore = idCreatore; }

    @Override
    public String toString() {
        return titolo + " | Scadenza: " + (scadenza != null ? scadenza : "N/D") + " | Stato: " + stato;
    }
}