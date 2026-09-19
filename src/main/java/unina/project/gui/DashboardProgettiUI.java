package unina.project.gui;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class DashboardProgettiUI extends Application {

    @Override
    public void start(Stage stage) {
        stage.setTitle("unina-taskboard - I Miei Progetti");

        Label lblTitolo = new Label("Progetti Disponibili");

        // Lista dei progetti a cui l'utente partecipa
        ListView<String> listaProgetti = new ListView<>();
        listaProgetti.getItems().addAll(
                "Progetto Esame OOP - Sviluppo App",
                "Progetto Basi di Dati - Gestione Libreria",
                "Progetto Reti - Chat Client/Server"
        );

        Button btnApriProgetto = new Button("Accedi al Progetto Selezionato");
        Button btnNuovoProgetto = new Button("Crea Nuovo Progetto");
        Label lblMsg = new Label();

        // Azione per aprire il progetto selezionato
        btnApriProgetto.setOnAction(e -> {
            String selezionato = listaProgetti.getSelectionModel().getSelectedItem();
            if (selezionato != null) {
                lblMsg.setText("Apertura di: " + selezionato);
                // TODO: Qui puoi aprire la schermata delle attività del progetto
            } else {
                lblMsg.setText("Seleziona prima un progetto dalla lista.");
            }
        });

        // Azione per creare un nuovo progetto (semplificata)
        btnNuovoProgetto.setOnAction(e -> {
            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("Nuovo Progetto");
            dialog.setHeaderText("Crea un nuovo progetto collaborativo");
            dialog.setContentText("Nome del progetto:");

            dialog.showAndWait().ifPresent(nomeProgetto -> {
                if (!nomeProgetto.trim().isEmpty()) {
                    listaProgetti.getItems().add(nomeProgetto);
                    lblMsg.setText("Progetto '" + nomeProgetto + "' creato con successo!");
                }
            });
        });

        VBox root = new VBox(10, lblTitolo, listaProgetti, btnApriProgetto, btnNuovoProgetto, lblMsg);
        root.setPadding(new Insets(20));

        stage.setScene(new Scene(root, 350, 400));
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}