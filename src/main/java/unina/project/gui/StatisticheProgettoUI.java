package unina.project.gui;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class StatisticheProgettoUI {

    public void start(Stage stage) {
        stage.setTitle("UninaTaskBoard - Statistiche e Report");
        BorderPane root = new BorderPane();

        // 1. MENU LATERALE (Coerente con il resto dell'applicazione)
        String stileMenu = "-fx-background-color: transparent; -fx-text-fill: #61dafb; -fx-cursor: hand; -fx-font-weight: bold; -fx-alignment: CENTER_LEFT; -fx-padding: 5;";
        Button btnProgetti = new Button("📁 Progetti");
        Button btnProfilo = new Button("👤 Profilo");
        Button btnReport = new Button("📊 Report");
        Button btnImpostazioni = new Button("⚙️ Impostazioni");
        Button btnEsci = new Button("🚪 Esci");

        for (Button b : new Button[]{btnProgetti, btnProfilo, btnReport, btnImpostazioni, btnEsci}) {
            b.setStyle(stileMenu);
        }

        btnEsci.setOnAction(e -> {
            stage.close();
            new LoginUI().start(new Stage());
        });

        Label lblMenu = new Label("MENU");
        lblMenu.setStyle("-fx-text-fill: #ffffff; -fx-font-weight: bold;");
        VBox sidebar = new VBox(12, lblMenu, new Separator(), btnProgetti, btnProfilo, btnReport, btnImpostazioni, new Separator(), btnEsci);
        sidebar.setPadding(new Insets(15));
        sidebar.setStyle("-fx-background-color: #1a1a1a;");

        // 2. CREAZIONE DEL GRAFICO A BARRE (con altezza fissa per non sovrapporsi)
        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Stato Attività");
        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Quantità");

        BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);
        barChart.setTitle("Report Statistiche Generali");
        barChart.setPrefHeight(240);
        barChart.setMaxHeight(240);

        XYChart.Series<String, Number> serie = new XYChart.Series<>();
        serie.setName("Task");
        serie.getData().add(new XYChart.Data<>("Completate", 3));
        serie.getData().add(new XYChart.Data<>("In Corso", 3));
        serie.getData().add(new XYChart.Data<>("Non Iniziate", 2));
        barChart.getData().add(serie);

        // 3. RIQUADRO CON I NOMI DEI RESPONSABILI (Posizionato sotto al grafico)
        Label lblDettaglioNomi = new Label(
                "📋 Dettaglio Responsabili per Stato:\n" +
                        "• Completate: Mario Rossi, Anna Verdi, Luigi Bianchi\n" +
                        "• In Corso: Mario Rossi, Luigi Bianchi, Giulia Neri\n" +
                        "• Non Iniziate: Anna Verdi, Giulia Neri"
        );
        lblDettaglioNomi.setStyle(
                "-fx-font-size: 13px; " +
                        "-fx-text-fill: #1e293b; " +
                        "-fx-padding: 12; " +
                        "-fx-background-color: #ffffff; " +
                        "-fx-border-color: #cbd5e1; " +
                        "-fx-border-radius: 6px; " +
                        "-fx-background-radius: 6px;"
        );
        lblDettaglioNomi.setWrapText(true);

        // 4. ASSEMBLAGGIO FINALE DEL PANNELLO REPORT
        Label lblTitoloReport = new Label("Report e Statistiche Avanzate");
        lblTitoloReport.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #1e293b;");

        VBox boxReportCentrale = new VBox(15, lblTitoloReport, barChart, lblDettaglioNomi);
        boxReportCentrale.setPadding(new Insets(20));
        boxReportCentrale.setStyle("-fx-background-color: #f8fafc;");

        // Inseriamo in un ScrollPane per evitare qualsiasi problema di visualizzazione su schermi piccoli
        ScrollPane scrollPane = new ScrollPane(boxReportCentrale);
        scrollPane.setFitToWidth(true);

        root.setLeft(sidebar);
        root.setCenter(scrollPane);

        stage.setScene(new Scene(root, 900, 600));
        stage.show();
    }
}