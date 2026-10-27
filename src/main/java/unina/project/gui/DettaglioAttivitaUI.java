package unina.project.gui;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import unina.project.controller.AttivitaController;
import unina.project.controller.FileController;
import unina.project.controller.ProgettoController;

import java.io.File;
import java.util.List;

public class DettaglioAttivitaUI {

    private final ProgettoController progettoController;
    private final AttivitaController attivitaController;
    private final FileController fileController;

    public DettaglioAttivitaUI(ProgettoController progettoController, AttivitaController attivitaController, FileController fileController) {
        this.progettoController = progettoController;
        this.attivitaController = attivitaController;
        this.fileController = fileController;
    }

    public VBox creaVistaDettaglio(BorderPane root, VBox vistaProgettoPrecedente) {
        // 1. INTESTAZIONE E TASTO INDIETRO
        Label lblTitolo = new Label("Dettaglio Attività: " + attivitaController.getTitoloAttivitaSelezionata());
        lblTitolo.setStyle("-fx-text-fill: #61dafb; -fx-font-weight: bold; -fx-font-size: 16px;");

        Button btnIndietro = new Button("⬅ Torna al Progetto");
        // Quando si torna indietro, rimettiamo al centro la vista di DettaglioProgettoUI
        btnIndietro.setOnAction(e -> root.setCenter(vistaProgettoPrecedente));

        // 2. GESTIONE STATO ATTIVITÀ
        HBox boxStato = new HBox(10);
        Label lblStato = new Label("Modifica Stato:");
        ComboBox<String> cmbStato = new ComboBox<>();
        cmbStato.getItems().addAll("(Nuova)", "(In Corso)", "(Completata)");

        Button btnAggiornaStato = new Button("Aggiorna Stato");
        btnAggiornaStato.setOnAction(e -> {
            if (cmbStato.getValue() != null) {
                // Metodo da aggiungere in AttivitaController:
                // attivitaController.aggiornaStatoAttivita(cmbStato.getValue());
                new Alert(Alert.AlertType.INFORMATION, "Stato aggiornato con successo!", ButtonType.OK).showAndWait();
            }
        });
        boxStato.getChildren().addAll(lblStato, cmbStato, btnAggiornaStato);

        // 3. GESTIONE COMMENTI
        VBox boxCommenti = new VBox(10);
        Label lblCommenti = new Label("Commenti:");
        lblCommenti.setStyle("-fx-font-weight: bold;");

        ListView<String> listaCommenti = new ListView<>();
        // Metodo da aggiungere in AttivitaController per caricare i commenti testuali:
        // listaCommenti.getItems().setAll(attivitaController.caricaCommentiAttivita());

        HBox boxNuovoCommento = new HBox(10);
        TextField txtCommento = new TextField();
        txtCommento.setPromptText("Scrivi un nuovo commento...");
        txtCommento.setPrefWidth(300);
        Button btnInviaCommento = new Button("Invia Commento");
        btnInviaCommento.setOnAction(e -> {
            if (!txtCommento.getText().isBlank()) {
                // Metodo da aggiungere in AttivitaController:
                // if (attivitaController.aggiungiCommento(txtCommento.getText().trim())) {
                //     listaCommenti.getItems().setAll(attivitaController.caricaCommentiAttivita());
                //     txtCommento.clear();
                // }
            }
        });
        boxNuovoCommento.getChildren().addAll(txtCommento, btnInviaCommento);
        boxCommenti.getChildren().addAll(lblCommenti, listaCommenti, boxNuovoCommento);

        // 4. GESTIONE FILE (Trasferita qui da DettaglioProgettoUI)
        VBox boxFile = new VBox(10);
        Label lblFile = new Label("File di Codice (Solo per attività di Sviluppo):");
        lblFile.setStyle("-fx-font-weight: bold;");

        HBox bottoniFile = new HBox(10);
        Button btnCarica = new Button("📤 Carica File");
        Button btnScarica = new Button("📥 Scarica Ultimo File");
        Button btnStorico = new Button("📜 Storico Revisioni");

        btnStorico.setOnAction(e -> {
            List<String> storico = fileController.getStoricoRevisioni();
            if (storico.isEmpty()) {
                new Alert(Alert.AlertType.INFORMATION, "Nessun file presente per questa attività.", ButtonType.OK).showAndWait();
                return;
            }
            Dialog<Void> dlg = new Dialog<>();
            dlg.setTitle("Cronologia Revisioni");
            dlg.setHeaderText("Storico file di: " + attivitaController.getTitoloAttivitaSelezionata());
            ListView<String> lv = new ListView<>();
            lv.getItems().addAll(storico);
            lv.setPrefSize(450, 300);
            dlg.getDialogPane().setContent(lv);
            dlg.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);
            dlg.showAndWait();
        });

        btnScarica.setOnAction(e -> {
            FileChooser fcDialog = new FileChooser();
            fcDialog.setInitialFileName(fileController.getNomeFileOriginale());
            File dest = fcDialog.showSaveDialog(root.getScene().getWindow());
            if (dest != null && fileController.scaricaUltimaRevisione(dest)) {
                new Alert(Alert.AlertType.INFORMATION, "Scaricato con successo.", ButtonType.OK).showAndWait();
            }
        });

        btnCarica.setOnAction(e -> {
            File scelto = new FileChooser().showOpenDialog(root.getScene().getWindow());
            if (scelto != null) {
                new TextInputDialog("Breve nota descrittiva").showAndWait().ifPresent(nota -> {
                    if (fileController.salvaFileERevisione(scelto, nota)) {
                        new Alert(Alert.AlertType.INFORMATION, "File e revisione salvati con successo!", ButtonType.OK).showAndWait();
                    } else {
                        new Alert(Alert.AlertType.ERROR, "Errore: i file sono ammessi solo per attività di Sviluppo.", ButtonType.OK).showAndWait();
                    }
                });
            }
        });

        bottoniFile.getChildren().addAll(btnCarica, btnScarica, btnStorico);
        boxFile.getChildren().addAll(lblFile, bottoniFile);

        // ASSEMBLAGGIO FINALE DEL LAYOUT
        VBox layoutPrincipale = new VBox(15,
                btnIndietro,
                lblTitolo,
                new Separator(),
                boxStato,
                new Separator(),
                boxCommenti,
                new Separator(),
                boxFile
        );
        layoutPrincipale.setPadding(new Insets(15));

        return layoutPrincipale;
    }
}