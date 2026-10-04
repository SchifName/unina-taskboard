package unina.project.gui;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import unina.project.controller.ProgettoController;
import unina.project.entity.Progetto;
import unina.project.entity.Utente;

import java.util.List;

public class DashboardProgettiUI {
    private final Utente utenteCorrente;
    private final ProgettoController progettoController = new ProgettoController();

    public DashboardProgettiUI(Utente utente){
        this.utenteCorrente = utente;
    }

    public void start(Stage stage) {
        stage.setTitle("UninaTaskBoard - Dashboard");
        BorderPane root = new BorderPane();

        // 1. PANNELLO PROGETTI
        ListView<Progetto> listaProgetti = new ListView<>();
        List<Progetto> progettiDalDB = progettoController.getProgettiUtente(utenteCorrente.getIdUtente());
        listaProgetti.getItems().addAll(progettiDalDB);

        String stileCeleste = "-fx-text-fill: #61dafb;";
        String stileTitolo = "-fx-text-fill: #61dafb; -fx-font-weight: bold; -fx-font-size: 14px;";

        Button btnApri = new Button("📂 Apri");
        Button btnNuovo = new Button("+ Nuovo");
        Button btnMod = new Button("✏️ Modifica");
        Button btnDel = new Button("🗑 Elimina");

        Runnable apriProgettoSelezionato = () -> {
            Progetto sel = listaProgetti.getSelectionModel().getSelectedItem();
            if (sel != null) {
                // Delega l'apertura alla classe dedicata DettaglioProgettoUI
                DettaglioProgettoUI dettaglioUI = new DettaglioProgettoUI(sel, utenteCorrente);
                root.setCenter(dettaglioUI.creaVistaDettaglio(root, listaProgetti));
            } else {
                new Alert(Alert.AlertType.WARNING, "Seleziona prima un progetto da aprire.", ButtonType.OK).showAndWait();
            }
        };

        btnApri.setOnAction(e -> apriProgettoSelezionato.run());
        listaProgetti.setOnMouseClicked(e -> { if (e.getClickCount() == 2) apriProgettoSelezionato.run(); });

        btnNuovo.setOnAction(e -> new TextInputDialog().showAndWait().ifPresent(s -> {
            if (!s.isBlank()) {
                Progetto nuovoP = new Progetto(0, s.trim(), "", utenteCorrente.getIdUtente());
                listaProgetti.getItems().add(nuovoP);
            }
        }));

        btnDel.setOnAction(e -> {
            Progetto sel = listaProgetti.getSelectionModel().getSelectedItem();
            if (sel != null) {
                listaProgetti.getItems().remove(sel);
            }
        });

        HBox bottoniProgetti = new HBox(10, btnApri, btnNuovo, btnMod, btnDel);
        VBox boxProgetti = new VBox(10, new Label("I Miei Progetti (Doppio click per aprire)"), bottoniProgetti, listaProgetti);
        boxProgetti.setPadding(new Insets(15));

        // 2. PANNELLO PROFILO
        Label lblProfTitolo = new Label("Profilo Utente");
        lblProfTitolo.setStyle(stileTitolo);
        Label lblNome = new Label("Nome: " + utenteCorrente.getNomeCompleto());
        Label lblRuolo = new Label("Ruolo: N/D");
        Label lblEmail = new Label("Email: " + utenteCorrente.getEmail());
        for(Label l : new Label[]{lblNome, lblRuolo, lblEmail}) l.setStyle(stileCeleste);

        VBox boxProfilo = new VBox(10, lblProfTitolo, lblNome, lblRuolo, lblEmail);
        boxProfilo.setPadding(new Insets(15));

        // 3. PANNELLO REPORT GLOBALE (Delegato a un metodo di supporto)
        VBox boxReport = creaVistaReportGlobale();

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
        bEsc.setOnAction(e -> { stage.close(); new LoginUI().start(new Stage()); });

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
        StringBuilder reportText = new StringBuilder();

        List<Progetto> progettiUtente = progettoController.getProgettiUtente(utenteCorrente.getIdUtente());
        for (Progetto p : progettiUtente) {
            reportText.append("📁 Progetto: ").append(p.getNome()).append("\n");
            reportText.append("\n");
        }

        TextArea txtReport = new TextArea(reportText.toString());
        txtReport.setEditable(false);
        txtReport.setPrefHeight(350);

        VBox box = new VBox(15, lbl, new Label("Elenco completo attività e relativi responsabili:"), txtReport);
        box.setPadding(new Insets(15));
        return box;
    }
}