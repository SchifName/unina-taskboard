package unina.project.entity;

import java.time.LocalDate;

public class Attivita {
    private int idAttivita;
    private int idProgetto;
    private String titolo;
    private String descrizione;
    private LocalDate scadenza;
    private String tipo;
    private String stato;
    private int idUtenteAssegnato;
    private String priorita;

    public Attivita(int idAttivita, int idProgetto, String titolo, String descrizione, LocalDate scadenza, String tipo, String stato, int idUtenteAssegnato, String priorita) {
        this.idAttivita = idAttivita;
        this.idProgetto = idProgetto;
        this.titolo = titolo;
        this.descrizione = descrizione;
        this.scadenza = scadenza;
        this.tipo = tipo;
        this.stato = stato;
        this.idUtenteAssegnato = idUtenteAssegnato;
        this.priorita = priorita;
    }

    public int getIdAttivita() { return idAttivita; }
    public int getIdProgetto() { return idProgetto; }
    public String getTitolo() { return titolo; }
    public String getDescrizione() { return descrizione; }
    public LocalDate getScadenza() { return scadenza; }
    public String getTipo() { return tipo; }
    public String getStato() { return stato; }
    public int getIdUtenteAssegnato() { return idUtenteAssegnato; }
    public String getPriorita() { return priorita; }

    public void setIdAttivita(int idAttivita) { this.idAttivita = idAttivita; }
    public void setIdProgetto(int idProgetto) { this.idProgetto = idProgetto; }
    public void setTitolo(String titolo) { this.titolo = titolo; }
    public void setDescrizione(String descrizione) { this.descrizione = descrizione; }
    public void setScadenza(LocalDate scadenza) { this.scadenza = scadenza; }
    public void setTipo(String tipo) { this.tipo = tipo; }
    public void setStato(String stato) { this.stato = stato; }
    public void setIdUtenteAssegnato(int idUtenteAssegnato) { this.idUtenteAssegnato = idUtenteAssegnato; }
    public void setPriorita(String priorita) { this.priorita = priorita; }

    @Override
    public String toString() {
        return titolo + " [Stato: " + stato + " | Priorità: " + (priorita != null ? priorita : "N/D") + "]";
    }
}