package unina.project.gui;

import javafx.geometry.Insets;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import unina.project.controller.AttivitaController;
import unina.project.controller.FileController;
import unina.project.controller.ProgettoController;

public class StatisticheProgettoUI {

    private final ProgettoController progettoController;
    private final AttivitaController attivitaController;
    private final FileController fileController;

    public StatisticheProgettoUI(ProgettoController progettoController, AttivitaController attivitaController, FileController fileController) {
        this.progettoController = progettoController;
        this.attivitaController = attivitaController;
        this.fileController = fileController;
    }

    public ScrollPane creaVistaStatistiche(BorderPane root, ListView<String> listaProgettiHome) {
        Label lblTitolo = new Label("📊 Report e Grafico a Cerchio - " + progettoController.getNomeProgettoSelezionato());
        lblTitolo.setStyle("-fx-text-fill: #61dafb; -fx-font-weight: bold; -fx-font-size: 14px;");

        Button btnTorna = new Button("⬅ Torna alle Attività");
        btnTorna.setOnAction(e -> {
            DettaglioProgettoUI dettaglioUI = new DettaglioProgettoUI(progettoController, attivitaController, fileController);
            root.setCenter(dettaglioUI.creaVistaDettaglio(root, listaProgettiHome));
        });

        // Chiede al Controller di aggiornare e restituire i conteggi elaborati
        attivitaController.aggiornaDatiStatistiche();
        int c = attivitaController.getNumeroCompletate();
        int ic = attivitaController.getNumeroInCorso();
        int ni = attivitaController.getNumeroNuove();

        PieChart pieChart = new PieChart();
        pieChart.setTitle("Distribuzione Stati Attività");
        if (c > 0) pieChart.getData().add(new PieChart.Data("Completate (" + c + ")", c));
        if (ic > 0) pieChart.getData().add(new PieChart.Data("In Corso (" + ic + ")", ic));
        if (ni > 0) pieChart.getData().add(new PieChart.Data("Non Iniziate (" + ni + ")", ni));
        pieChart.setPrefSize(400, 250);

        TextArea txtDettagli = new TextArea(attivitaController.getTestoDettaglioStatistiche());
        txtDettagli.setEditable(false);
        txtDettagli.setPrefHeight(180);

        VBox box = new VBox(15, btnTorna, lblTitolo, pieChart, new Label("Dettaglio Attività per Stato:"), txtDettagli);
        box.setPadding(new Insets(15));

        ScrollPane scrollPane = new ScrollPane(box);
        scrollPane.setFitToWidth(true);
        return scrollPane;
    }
}