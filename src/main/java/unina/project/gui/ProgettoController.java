package unina.project.gui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProgettoController {

    // Lista dei progetti in memoria
    private static final List<String> progettiMemoria = new ArrayList<>(List.of("Progetto Esame OOP", "Sviluppo App Mobile"));

    // Mappa che associa a ogni progetto la sua lista di attività
    private static final Map<String, List<String>> mappaAttivitaMemoria = new HashMap<>();

    static {
        // Inizializzazione dati di esempio per le attività
        mappaAttivitaMemoria.put("Progetto Esame OOP", new ArrayList<>(List.of("Analisi Requisiti (Completata)", "Implementazione GUI (In Corso)")));
        mappaAttivitaMemoria.put("Sviluppo App Mobile", new ArrayList<>(List.of("Setup Ambiente (Completata)", "Design schermata Login (Non Iniziata)")));
    }

    // Restituisce l'elenco dei progetti dell'utente
    public List<String> getNomiProgettiUtente() {
        return progettiMemoria;
    }

    // Crea un nuovo progetto
    public void creaNuovoProgetto(String nome) {
        if (nome != null && !nome.trim().isEmpty()) {
            String nomePulito = nome.trim();
            if (!progettiMemoria.contains(nomePulito)) {
                progettiMemoria.add(nomePulito);
                mappaAttivitaMemoria.putIfAbsent(nomePulito, new ArrayList<>());
            }
        }
    }

    // Modifica il nome di un progetto esistente
    public void modificaProgetto(int index, String nuovoNome) {
        if (index >= 0 && index < progettiMemoria.size() && nuovoNome != null && !nuovoNome.trim().isEmpty()) {
            String vecchioNome = progettiMemoria.get(index);
            String nomePulito = nuovoNome.trim();

            progettiMemoria.set(index, nomePulito);

            // Sposta le attività associate dal vecchio al nuovo nome
            List<String> attivita = mappaAttivitaMemoria.remove(vecchioNome);
            mappaAttivitaMemoria.put(nomePulito, attivita != null ? attivita : new ArrayList<>());
        }
    }

    // Elimina un progetto
    public void eliminaProgetto(String nome) {
        if (nome != null) {
            progettiMemoria.remove(nome);
            mappaAttivitaMemoria.remove(nome);
        }
    }

    // Restituisce le attività di uno specifico progetto
    public List<String> getAttivitaProgetto(String nomeProgetto) {
        return mappaAttivitaMemoria.computeIfAbsent(nomeProgetto, k -> new ArrayList<>());
    }

    // Aggiunge un'attività a un progetto
    public void aggiungiAttivita(String nomeProgetto, String descrizioneAttivita) {
        if (nomeProgetto != null && descrizioneAttivita != null && !descrizioneAttivita.trim().isEmpty()) {
            mappaAttivitaMemoria.computeIfAbsent(nomeProgetto, k -> new ArrayList<>()).add(descrizioneAttivita.trim());
        }
    }

    // Rimuove un'attività da un progetto
    public void rimuoviAttivita(String nomeProgetto, String descrizioneAttivita) {
        List<String> attivita = mappaAttivitaMemoria.get(nomeProgetto);
        if (attivita != null) {
            attivita.remove(descrizioneAttivita);
        }
    }
}