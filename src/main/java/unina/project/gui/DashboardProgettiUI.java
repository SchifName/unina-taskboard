package unina.project.gui;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import unina.project.controller.AttivitaController;
import unina.project.controller.AutenticazioneController;
import unina.project.controller.FileController;
import unina.project.controller.ProgettoController;

public class DashboardProgettiUI {

    private final AutenticazioneController authController;
    private final ProgettoController progettoController;
    private final AttivitaController attivitaController;
    private final FileController fileController;

    public DashboardProgettiUI(AutenticazioneController authController, ProgettoController progettoController, AttivitaController attivitaController, FileController fileController) {
        this.authController = authController;
        this.progettoController = progettoController;
        this.attivitaController = attivitaController;
        this.fileController = fileController;
    }

    public void start(Stage stage) {
        stage.setTitle("UninaTaskBoard - Dashboard");
        BorderPane root = new BorderPane();

        // 1. PANNELLO PROGETTI
        ListView<String> listaProgetti = new ListView<>();
        listaProgetti.getItems().setAll(progettoController.caricaNomiProgettiUtente());

        String stileCeleste = "-fx-text-fill: #61dafb;";
        String stileTitolo = "-fx-text-fill: #61dafb; -fx-font-weight: bold; -fx-font-size: 14px;";

        Button btnApri = new Button("📂 Apri");
        Button btnNuovo = new Button("+ Nuovo");
        Button btnMod = new Button("✏️ Modifica");
        Button btnDel = new Button("🗑 Elimina");

        Runnable apriProgettoSelezionato = () -> {
            int idx = listaProgetti.getSelectionModel().getSelectedIndex();
            if (progettoController.selezionaProgetto(idx)) {
                DettaglioProgettoUI dettaglioUI = new DettaglioProgettoUI(progettoController, attivitaController, fileController);
                root.setCenter(dettaglioUI.creaVistaDettaglio(root, listaProgetti));
            } else {
                new Alert(Alert.AlertType.WARNING, "Seleziona prima un progetto da aprire.", ButtonType.OK).showAndWait();
            }
        };

        btnApri.setOnAction(e -> apriProgettoSelezionato.run());
        listaProgetti.setOnMouseClicked(e -> { if (e.getClickCount() == 2) apriProgettoSelezionato.run(); });

        btnNuovo.setOnAction(event -> {
            Dialog<String[]> dialog = new Dialog<>();
            dialog.setTitle("NUOVO PROGETTO");
            dialog.setHeaderText("Inserisci i dati del nuovo progetto");

            Label lblNome = new Label("Nome del progetto:");
            TextField txtNome = new TextField();
            lblNome.setText("Es. Esame 001");

            Label lblDescrizione = new Label("Descrizione:");
            TextField txtDescrizione = new TextField();
            lblDescrizione.setText("Breve descrizione...");

            VBox Contenuto = new VBox(10);
            Contenuto.getChildren().addAll(lblNome, txtNome, lblDescrizione, txtDescrizione);
            dialog.getDialogPane().setContent(Contenuto);

            dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
            dialog.setResultConverter(dialogButton -> {
                if(dialogButton.equals(ButtonType.OK)){
                    return new String[]{
                            txtNome.getText().trim(),
                            txtDescrizione.getText().trim()
                    };
                }
                return null;
            });

            dialog.showAndWait().ifPresent(risultato -> {
                String nome = risultato[0];
                String descrizione = risultato[1];

                // Validazione e chiamata al controller (che comunica poi con il DB)
                if (!nome.isBlank() && progettoController.creaNuovoProgetto(nome, descrizione)) {
                    listaProgetti.getItems().setAll(progettoController.caricaNomiProgettiUtente());
                }
            });

        });

        btnDel.setOnAction(e -> {
            int idx = listaProgetti.getSelectionModel().getSelectedIndex();
            if (progettoController.eliminaProgetto(idx)) {
                listaProgetti.getItems().remove(idx);
            }
        });

        HBox bottoniProgetti = new HBox(10, btnApri, btnNuovo, btnMod, btnDel);
        VBox boxProgetti = new VBox(10, new Label("I Miei Progetti (Doppio click per aprire)"), bottoniProgetti, listaProgetti);
        boxProgetti.setPadding(new Insets(15));

        // 2. PANNELLO PROFILO (Dati chiesti al Controller senza toccare l'Entity Utente)
        Label lblProfTitolo = new Label("Profilo Utente");
        lblProfTitolo.setStyle(stileTitolo);
        Label lblNome = new Label("Nome: " + authController.getNomeCompletoUtente());
        Label lblEmail = new Label("Email: " + authController.getEmailUtente());
        for (Label l : new Label[]{lblNome, lblEmail}) l.setStyle(stileCeleste);

        VBox boxProfilo = new VBox(10, lblProfTitolo, lblNome, lblEmail);
        boxProfilo.setPadding(new Insets(15));

        // 4. PANNELLO IMPOSTAZIONI
        CheckBox chkDark = new CheckBox("Modalità Oscura");
        Slider sliderZoom = new Slider(10, 20, 12);
        chkDark.setStyle(stileCeleste);
        Runnable aggiornaTema = () -> {
            boolean dark = chkDark.isSelected();
            String bg = dark ? "-fx-background-color: #2b2b2b;" : "-fx-background-color: #ffffff;";
            root.setStyle(bg + " -fx-font-size: " + sliderZoom.getValue() + "px;");
        };
        chkDark.setOnAction(e -> aggiornaTema.run());
        sliderZoom.valueProperty().addListener(e -> aggiornaTema.run());

        VBox boxImpostazioni = new VBox(10, new Label("Impostazioni"), chkDark, new Label("Zoom Testo:"), sliderZoom);
        boxImpostazioni.setPadding(new Insets(15));

        // 5. MENU LATERALE (Sidebar)
        String styleBtn = "-fx-background-color: transparent; -fx-text-fill: #61dafb; -fx-cursor: hand; -fx-font-weight: bold; -fx-alignment: CENTER_LEFT;";
        Button bP = new Button("📁 Progetti");
        Button bProf = new Button("👤 Profilo");
        Button bRep = new Button("📊 Report");
        Button bSet = new Button("⚙️ Impostazioni");
        Button bEsc = new Button("🚪 Esci");
        for (Button b : new Button[]{bP, bProf, bRep, bSet, bEsc}) b.setStyle(styleBtn);

        bP.setOnAction(e -> root.setCenter(boxProgetti));
        bProf.setOnAction(e -> root.setCenter(boxProfilo));
        bRep.setOnAction(e -> root.setCenter(creaVistaReportGlobale()));
        bSet.setOnAction(e -> root.setCenter(boxImpostazioni));
        bEsc.setOnAction(e -> {
            authController.effettuaLogout();
            stage.close();
            new LoginUI(authController, progettoController, attivitaController, fileController).start(new Stage());
        });

        VBox sidebar = new VBox(12, new Label("MENU"), new Separator(), bP, bProf, bRep, bSet, new Separator(), bEsc);
        sidebar.setPadding(new Insets(15));
        sidebar.setStyle("-fx-background-color: #1a1a1a;");

        root.setLeft(sidebar);
        root.setCenter(boxProgetti);

        stage.setScene(new Scene(root, 900, 550));
        stage.show();
    }

    private VBox creaVistaReportGlobale() {
        Label lbl = new Label("📊 Report Globale di Tutti i Progetti");
        lbl.setStyle("-fx-text-fill: #61dafb; -fx-font-weight: bold; -fx-font-size: 14px;");

        TextArea txtReport = new TextArea(progettoController.generaReportGlobale());
        txtReport.setEditable(false);
        txtReport.setPrefHeight(350);

        VBox box = new VBox(15, lbl, new Label("Elenco completo progetti:"), txtReport);
        box.setPadding(new Insets(15));
        return box;
    }
}