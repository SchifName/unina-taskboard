package unina.project.gui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class DettaglioProgettoUI {

    public void mostra(String nomeProgetto) {
        Stage stage = new Stage();
        stage.setTitle("Progetto: " + nomeProgetto);
        BorderPane root = new BorderPane();

        // 1. Intestazione in alto
        Label lblTitolo = new Label(nomeProgetto + " [Software]");
        lblTitolo.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
        Label lblCreatore = new Label("Creato da: admin");

        VBox topBox = new VBox(5, lblTitolo, lblCreatore);
        topBox.setPadding(new Insets(10));

        // 2. Elenco Attività al centro
        ListView<String> listaAttivita = new ListView<>();
        listaAttivita.getItems().addAll(
                "Michele | Stato: Da Iniziare",
                "Ciao a tutti | Stato: Da Iniziare"
        );

        VBox centerBox = new VBox(10, new Label("Elenco Attività:"), listaAttivita);
        centerBox.setPadding(new Insets(10));

        // 3. Grafico a torta (PieChart) a destra per le statistiche degli stati
        PieChart pieChart = new PieChart();
        pieChart.getData().addAll(
                new PieChart.Data("Da Iniziare (2)", 2),
                new PieChart.Data("In Corso (0)", 0),
                new PieChart.Data("Completato (0)", 0)
        );
        pieChart.setPrefSize(250, 200);

        VBox rightBox = new VBox(10, new Label("Stato Attività"), pieChart);
        rightBox.setPadding(new Insets(10));

        // 4. Pulsanti di gestione in basso
        Button btnNuovaAttivita = new Button("Nuova Attività");
        Button btnInvita = new Button("Invita Collaboratore");
        Button btnAssegna = new Button("Assegna Utente");
        Button btnCambiaStato = new Button("Cambia Stato");

        HBox bottomBox = new HBox(10, btnNuovaAttivita, btnInvita, btnAssegna, btnCambiaStato);
        bottomBox.setAlignment(Pos.CENTER);
        bottomBox.setPadding(new Insets(10));

        // Azioni dei pulsanti (dimostrative)
        btnNuovaAttivita.setOnAction(e -> {
            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("Nuova Attività");
            dialog.setHeaderText("Inserisci il titolo della nuova attività:");
            dialog.showAndWait().ifPresent(nuova -> listaAttivita.getItems().add(nuova + " | Stato: Da Iniziare"));
        });

        // Assemblaggio della schermata
        root.setTop(topBox);
        root.setCenter(centerBox);
        root.setRight(rightBox);
        root.setBottom(bottomBox);

        stage.setScene(new Scene(root, 750, 420));
        stage.show();
    }
}