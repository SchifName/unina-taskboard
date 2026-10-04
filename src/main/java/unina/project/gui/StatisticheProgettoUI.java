package unina.project.gui;

import javafx.geometry.Insets;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import unina.project.controller.AttivitaController;
import unina.project.entity.Attivita;
import unina.project.entity.Progetto;
import unina.project.entity.Utente;

import java.util.List;

public class StatisticheProgettoUI {
    private final Progetto progettoSelezionato;
    private final Utente utenteCorrente;
    private final AttivitaController attivitaController = new AttivitaController();

    public StatisticheProgettoUI(Progetto progetto, Utente utente) {
        this.progettoSelezionato = progetto;
        this.utenteCorrente = utente;
    }

    public ScrollPane creaVistaStatistiche(BorderPane root, ListView<Progetto> listaProgettiHome) {
        Label lblTitolo = new Label("📊 Report e Grafico a Cerchio - " + progettoSelezionato.getNome());
        lblTitolo.setStyle("-fx-text-fill: #61dafb; -fx-font-weight: bold; -fx-font-size: 14px;");

        Button btnTorna = new Button("⬅ Torna alle Attività");
        btnTorna.setOnAction(e -> {
            DettaglioProgettoUI dettaglioUI = new DettaglioProgettoUI(progettoSelezionato, utenteCorrente);
            root.setCenter(dettaglioUI.creaVistaDettaglio(root, listaProgettiHome));
        });

        List<Attivita> attivita = attivitaController.getAttivitaByProgetto(progettoSelezionato.getIdProgetto());

        int c = 0, ic = 0, ni = 0;
        StringBuilder completateNomi = new StringBuilder();
        StringBuilder inCorsoNomi = new StringBuilder();
        StringBuilder nonIniziateNomi = new StringBuilder();

        for (Attivita task : attivita) {
            if ("Finita".equals(task.getStato())) {
                c++;
                completateNomi.append("• ").append(task).append("\n");
            } else if ("Presa_in_carico".equals(task.getStato())) {
                ic++;
                inCorsoNomi.append("• ").append(task).append("\n");
            } else {
                ni++;
                nonIniziateNomi.append("• ").append(task).append("\n");
            }
        }

        // Configurazione del Grafico a Cerchio (PieChart)
        PieChart pieChart = new PieChart();
        pieChart.setTitle("Distribuzione Stati Attività");
        if (c > 0) pieChart.getData().add(new PieChart.Data("Completate (" + c + ")", c));
        if (ic > 0) pieChart.getData().add(new PieChart.Data("In Corso (" + ic + ")", ic));
        if (ni > 0) pieChart.getData().add(new PieChart.Data("Non Iniziate (" + ni + ")", ni));
        pieChart.setPrefSize(400, 250);

        TextArea txtDettagli = new TextArea(
                "✅ COMPLETATE (" + c + "):\n" + (completateNomi.length() > 0 ? completateNomi.toString() : "Nessuna\n") + "\n\n" +
                        "⏳ IN CORSO (" + ic + "):\n" + (inCorsoNomi.length() > 0 ? inCorsoNomi.toString() : "Nessuna\n") + "\n\n" +
                        "❌ NUOVE (" + ni + "):\n" + (nonIniziateNomi.length() > 0 ? nonIniziateNomi.toString() : "Nessuna\n")
        );
        txtDettagli.setEditable(false);
        txtDettagli.setPrefHeight(180);

        VBox box = new VBox(15, btnTorna, lblTitolo, pieChart, new Label("Dettaglio Attività per Stato:"), txtDettagli);
        box.setPadding(new Insets(15));

        ScrollPane scrollPane = new ScrollPane(box);
        scrollPane.setFitToWidth(true);
        return scrollPane;
    }
}