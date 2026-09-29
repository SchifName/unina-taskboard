package unina.project.gui;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import unina.project.controller.ProgettoController;
import unina.project.entity.Utente;
import unina.project.entity.Progetto;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DashboardProgettiUI {
    private final Utente utenteCorrente;
    private final ProgettoController progettoController = new ProgettoController();
    private final Map<String, List<String>> mappaAttivita = new HashMap<>();

    public DashboardProgettiUI(Utente utente){
        this.utenteCorrente = utente;
    }

    public void start(Stage stage) {
        stage.setTitle("UninaTaskBoard - Dashboard");
        BorderPane root = new BorderPane();

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
                root.setCenter(creaVistaDettaglioProgetto(sel.getNome(), root, listaProgetti));
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
                mappaAttivita.put(s.trim(), new ArrayList<>());
            }
        }));

        btnDel.setOnAction(e -> {
            Progetto sel = listaProgetti.getSelectionModel().getSelectedItem();
            if (sel != null) {
                listaProgetti.getItems().remove(sel);
                mappaAttivita.remove(sel.getNome());
            }
        });

        HBox bottoniProgetti = new HBox(10, btnApri, btnNuovo, btnMod, btnDel);
        VBox boxProgetti = new VBox(10, new Label("I Miei Progetti (Doppio click per aprire)"), bottoniProgetti, listaProgetti);
        boxProgetti.setPadding(new Insets(15));

        // 2. PANNELLO PROFILO
        Label lblProfTitolo = new Label("Profilo Utente");
        lblProfTitolo.setStyle(stileTitolo);
        Label lblNome = new Label("Nome: " + utenteCorrente.getNomeCompleto());
        Label lblRuolo = new Label("Ruolo: " + "");
        Label lblEmail = new Label("Email: " + utenteCorrente.getEmail());
        for(Label l : new Label[]{lblNome, lblRuolo, lblEmail}) l.setStyle(stileCeleste);

        VBox boxProfilo = new VBox(10, lblProfTitolo, lblNome, lblRuolo, lblEmail);
        boxProfilo.setPadding(new Insets(15));

        // 3. PANNELLO REPORT GLOBALE
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

        // 5. MENU LATERALE
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

    private VBox creaVistaDettaglioProgetto(String nomeProgetto, BorderPane root, ListView<Progetto> listaProgettiHome) {
        String stileTitolo = "-fx-text-fill: #61dafb; -fx-font-weight: bold; -fx-font-size: 14px;";
        Label lblTitolo = new Label("Progetto: " + nomeProgetto);
        lblTitolo.setStyle(stileTitolo);

        Button btnIndietro = new Button("⬅ Torna ai Progetti");
        btnIndietro.setOnAction(e -> root.setCenter(listaProgettiHome.getParent()));

        List<String> attivitaDelProgetto = mappaAttivita.computeIfAbsent(nomeProgetto, k -> new ArrayList<>());
        ListView<String> listaAttivitaUI = new ListView<>();
        listaAttivitaUI.getItems().addAll(attivitaDelProgetto);

        Button btnNuovaAttivita = new Button("+ Nuova Attività");
        Button btnEliminaAttivita = new Button("🗑 Elimina");
        Button btnStatisticheProgetto = new Button("📈 Grafico Statistiche Progetto");

        btnNuovaAttivita.setOnAction(e -> {
            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setTitle("Nuova Attività");
            dialog.setHeaderText("Inserisci i dati della task");

            TextField txtTitolo = new TextField();
            txtTitolo.setPromptText("Titolo Attività");
            TextField txtAssegnatore = new TextField();
            txtAssegnatore.setPromptText("Membro Responsabile (es. Mario Rossi)");

            ComboBox<String> cmbStato = new ComboBox<>();
            cmbStato.getItems().addAll("(Completata)", "(In Corso)", "(Non Iniziata)");
            cmbStato.setValue("(In Corso)");

            VBox content = new VBox(10, new Label("Titolo:"), txtTitolo, new Label("Assegnato a:"), txtAssegnatore, new Label("Stato:"), cmbStato);
            dialog.getDialogPane().setContent(content);
            dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

            dialog.showAndWait().ifPresent(res -> {
                if (res == ButtonType.OK && !txtTitolo.getText().isBlank() && !txtAssegnatore.getText().isBlank()) {
                    String taskFormattata = txtTitolo.getText().trim() + " | Assegnato a: " + txtAssegnatore.getText().trim() + " " + cmbStato.getValue();
                    attivitaDelProgetto.add(taskFormattata);
                    listaAttivitaUI.getItems().add(taskFormattata);
                }
            });
        });

        btnEliminaAttivita.setOnAction(e -> {
            String sel = listaAttivitaUI.getSelectionModel().getSelectedItem();
            if (sel != null) { attivitaDelProgetto.remove(sel); listaAttivitaUI.getItems().remove(sel); }
        });

        btnStatisticheProgetto.setOnAction(e -> {
            root.setCenter(creaVistaStatisticheProgetto(nomeProgetto, root, listaProgettiHome));
        });

        HBox bottoniTask = new HBox(10, btnNuovaAttivita, btnEliminaAttivita, btnStatisticheProgetto);
        VBox boxDettaglio = new VBox(12, btnIndietro, lblTitolo, new Label("Elenco Attività:"), listaAttivitaUI, bottoniTask);
        boxDettaglio.setPadding(new Insets(15));
        return boxDettaglio;
    }

    /**
     * Schermata dedicata alle statistiche del singolo progetto con GRAFICO A CERCHIO (PieChart) e i nomi
     */
    private ScrollPane creaVistaStatisticheProgetto(String nomeProgetto, BorderPane root, ListView<Progetto> listaProgettiHome) {
        Label lblTitolo = new Label("📊 Report e Grafico a Cerchio - " + nomeProgetto);
        lblTitolo.setStyle("-fx-text-fill: #61dafb; -fx-font-weight: bold; -fx-font-size: 14px;");

        Button btnTorna = new Button("⬅ Torna alle Attività");
        btnTorna.setOnAction(e -> root.setCenter(creaVistaDettaglioProgetto(nomeProgetto, root, listaProgettiHome)));

        List<String> attivita = mappaAttivita.getOrDefault(nomeProgetto, new ArrayList<>());

        int c = 0, ic = 0, ni = 0;
        StringBuilder completateNomi = new StringBuilder();
        StringBuilder inCorsoNomi = new StringBuilder();
        StringBuilder nonIniziateNomi = new StringBuilder();

        for (String task : attivita) {
            if (task.contains("(Completata)")) {
                c++;
                completateNomi.append("• ").append(task).append("\n");
            } else if (task.contains("(In Corso)")) {
                ic++;
                inCorsoNomi.append("• ").append(task).append("\n");
            } else if (task.contains("(Non Iniziata)")) {
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
                        "❌ NON INIZIATE (" + ni + "):\n" + (nonIniziateNomi.length() > 0 ? nonIniziateNomi.toString() : "Nessuna\n")
        );
        txtDettagli.setEditable(false);
        txtDettagli.setPrefHeight(180);

        VBox box = new VBox(15, btnTorna, lblTitolo, pieChart, new Label("Dettaglio Responsabili per Stato:"), txtDettagli);
        box.setPadding(new Insets(15));

        ScrollPane scrollPane = new ScrollPane(box);
        scrollPane.setFitToWidth(true);
        return scrollPane;
    }

    private VBox creaVistaReportGlobale() {
        Label lbl = new Label("📊 Report Globale di Tutti i Progetti");
        lbl.setStyle("-fx-text-fill: #61dafb; -fx-font-weight: bold; -fx-font-size: 14px;");

        StringBuilder reportText = new StringBuilder();
        for (Map.Entry<String, List<String>> entry : mappaAttivita.entrySet()) {
            reportText.append("📁 Progetto: ").append(entry.getKey()).append("\n");
            for (String t : entry.getValue()) {
                reportText.append("   ").append(t).append("\n");
            }
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