package unina.project.gui;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import unina.project.controller.AttivitaController;
import unina.project.controller.ProgettoController;
import unina.project.controller.FileController;
import unina.project.entity.Attivita;
import unina.project.entity.Utente;
import unina.project.entity.Progetto;
import java.io.File;
import java.util.List;

public class DashboardProgettiUI {
    private final Utente utenteCorrente;
    private final ProgettoController progettoController = new ProgettoController();
    private final AttivitaController attivitaController = new AttivitaController();

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
                root.setCenter(creaVistaDettaglioProgetto(sel, root, listaProgetti));
            } else {
                new Alert(Alert.AlertType.WARNING, "Seleziona prima un progetto da aprire.", ButtonType.OK).showAndWait();
            }
        };

        btnApri.setOnAction(e -> apriProgettoSelezionato.run());
        listaProgetti.setOnMouseClicked(e -> { if (e.getClickCount() == 2) apriProgettoSelezionato.run(); });

        btnNuovo.setOnAction(e -> new TextInputDialog().showAndWait().ifPresent(s -> {
            if (!s.isBlank()) {
                // Crea un progetto visivo temporaneo
                Progetto nuovoP = new Progetto(0, s.trim(), "", utenteCorrente.getIdUtente());
                listaProgetti.getItems().add(nuovoP);
            }
        }));

        btnDel.setOnAction(e -> {
            Progetto sel = listaProgetti.getSelectionModel().getSelectedItem();
            if (sel != null) {
                // Rimuove il progetto solo dalla lista visiva
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

    private VBox creaVistaDettaglioProgetto(Progetto progettoSelezionato, BorderPane root, ListView<Progetto> listaProgettiHome) {
        String stileTitolo = "-fx-text-fill: #61dafb; -fx-font-weight: bold; -fx-font-size: 14px;";
        Label lblTitolo = new Label("Progetto: " + progettoSelezionato.getNome());
        lblTitolo.setStyle(stileTitolo);

        Button btnIndietro = new Button("⬅ Torna ai Progetti");
        btnIndietro.setOnAction(e -> root.setCenter(listaProgettiHome.getParent()));

        ListView<Attivita> listaAttivitaUI = new ListView<>();
        List<Attivita> attivitaDalDB = attivitaController.getAttivitaByProgetto(progettoSelezionato.getIdProgetto());
        listaAttivitaUI.getItems().addAll(attivitaDalDB);

        Button btnNuovaAttivita = new Button("+ Nuova Attività");
        Button btnCaricaFile = new Button("📤 Carica File");
        Button btnScaricaFile = new Button("📥 Scarica Ultimo File");

        btnNuovaAttivita.setOnAction(e -> {
            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setTitle("Nuova Attività");
            dialog.setHeaderText("Inserisci i dettagli completi dell'attività");

            TextField txtTitolo = new TextField();
            txtTitolo.setPromptText("Titolo (obbligatorio)");

            TextField txtDescrizione = new TextField();
            txtDescrizione.setPromptText("Descrizione breve");

            DatePicker dpScadenza = new DatePicker();
            dpScadenza.setPromptText("Seleziona una data di scadenza");
            dpScadenza.setPrefWidth(300);

            TextField txtTipo = new TextField();
            txtTipo.setPromptText("Tipo (es. Sviluppo, Documentazione, Bugfix)");

            ComboBox<String> cmbStato = new ComboBox<>();
            cmbStato.getItems().addAll("(Completata)", "(In Corso)", "(Non Iniziata)");
            cmbStato.setValue("(Non Iniziata)");

            VBox content = new VBox(10,
                    new Label("Titolo:"), txtTitolo,
                    new Label("Descrizione:"), txtDescrizione,
                    new Label("Scadenza:"), dpScadenza,
                    new Label("Tipo Task:"), txtTipo,
                    new Label("Stato:"), cmbStato
            );
            dialog.getDialogPane().setContent(content);
            dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

            dialog.showAndWait().ifPresent(res -> {
                if (res == ButtonType.OK && !txtTitolo.getText().isBlank()) {
                    String statoDB = "Nuova";
                    if (cmbStato.getValue().equals("(Completata)")) statoDB = "Finita";
                    else if (cmbStato.getValue().equals("(In Corso)")) statoDB = "Presa_in_carico";

                    // Chiamiamo il metodo del controller passandogli TUTTI i parametri della UI
                    boolean successo = attivitaController.inserisciAttivita(
                            progettoSelezionato.getIdProgetto(),
                            txtTitolo.getText().trim(),
                            txtDescrizione.getText().trim(),
                            dpScadenza.getValue(), // Prende in automatico la data (LocalDate) o null
                            txtTipo.getText().trim(),
                            statoDB,
                            utenteCorrente.getIdUtente()
                    );

                    if (successo) {
                        listaAttivitaUI.getItems().clear();
                        listaAttivitaUI.getItems().addAll(attivitaController.getAttivitaByProgetto(progettoSelezionato.getIdProgetto()));
                    } else {
                        new Alert(Alert.AlertType.ERROR, "Errore nel salvataggio su database.", ButtonType.OK).showAndWait();
                    }
                }
            });
        });

        btnScaricaFile.setOnAction(e -> {
            Attivita taskSelezionata = listaAttivitaUI.getSelectionModel().getSelectedItem();

            if (taskSelezionata == null) {
                new Alert(Alert.AlertType.WARNING, "Seleziona un'attività per scaricare il suo file.", ButtonType.OK).showAndWait();
                return;
            }

            FileController fc = new FileController();

            // 1. Chiediamo al DB come si chiamava il file originale!
            String nomeOriginale = fc.getNomeFileOriginale(taskSelezionata.getIdAttivita());

            javafx.stage.FileChooser fileChooser = new javafx.stage.FileChooser();
            fileChooser.setTitle("Salva il file scaricato");
            // 2. Impostiamo il VERO nome come suggerimento di salvataggio
            fileChooser.setInitialFileName(nomeOriginale);

            File destinazione = fileChooser.showSaveDialog(root.getScene().getWindow());

            if (destinazione != null) {
                boolean successo = fc.scaricaUltimaRevisione(taskSelezionata.getIdAttivita(), destinazione);

                if (successo) {
                    new Alert(Alert.AlertType.INFORMATION, "File scaricato con successo in:\n" + destinazione.getAbsolutePath(), ButtonType.OK).showAndWait();
                } else {
                    new Alert(Alert.AlertType.ERROR, "Errore: impossibile scaricare il file.", ButtonType.OK).showAndWait();
                }
            }
        });

        btnCaricaFile.setOnAction(e -> {
            // Capiamo quale attività è stata selezionata nella lista
            Attivita taskSelezionata = listaAttivitaUI.getSelectionModel().getSelectedItem();

            if (taskSelezionata == null) {
                new Alert(Alert.AlertType.WARNING, "Devi prima selezionare un'attività dalla lista!", ButtonType.OK).showAndWait();
                return;
            }

            // Apriamo la finestra nativa del computer per scegliere il file
            javafx.stage.FileChooser fileChooser = new javafx.stage.FileChooser();
            fileChooser.setTitle("Seleziona il file di codice da caricare");
            File fileScelto = fileChooser.showOpenDialog(root.getScene().getWindow());

            if (fileScelto != null) {
                // Chiediamo la nota descrittiva (es. "Prima bozza", "Bug fix")
                TextInputDialog dialogNota = new TextInputDialog("Prima versione");
                dialogNota.setTitle("Nota Descrittiva");
                dialogNota.setHeaderText("Inserisci una breve nota per questa revisione:");

                dialogNota.showAndWait().ifPresent(nota -> {
                    FileController fc = new FileController();
                    boolean successo = fc.salvaFileERevisione(
                            taskSelezionata.getIdAttivita(),
                            utenteCorrente.getIdUtente(),
                            fileScelto,
                            nota
                    );

                    if (successo) {
                        new Alert(Alert.AlertType.INFORMATION, "🎉 File salvato con successo in PostgreSQL!", ButtonType.OK).showAndWait();
                    } else {
                        new Alert(Alert.AlertType.ERROR, "Errore durante il salvataggio del file.", ButtonType.OK).showAndWait();
                    }
                });
            }
        });

        Button btnStatisticheProgetto = new Button("📈 Grafico Statistiche Progetto");

        btnStatisticheProgetto.setOnAction(e -> {
            root.setCenter(creaVistaStatisticheProgetto(progettoSelezionato, root, listaProgettiHome));
        });

        btnStatisticheProgetto.setOnAction(e -> {
            root.setCenter(creaVistaStatisticheProgetto(progettoSelezionato, root, listaProgettiHome));
        });

        HBox bottoniTask = new HBox(10, btnNuovaAttivita, btnScaricaFile, btnCaricaFile, btnStatisticheProgetto);
        VBox boxDettaglio = new VBox(12, btnIndietro, lblTitolo, new Label("Elenco Attività:"), listaAttivitaUI, bottoniTask);
        boxDettaglio.setPadding(new Insets(15));
        return boxDettaglio;
    }

    /**
     * Schermata dedicata alle statistiche del singolo progetto con GRAFICO A CERCHIO (PieChart) e i nomi
     */
    private ScrollPane creaVistaStatisticheProgetto(Progetto progettoSelezionato, BorderPane root, ListView<Progetto> listaProgettiHome) {
        Label lblTitolo = new Label("📊 Report e Grafico a Cerchio - " + progettoSelezionato.getNome());
        lblTitolo.setStyle("-fx-text-fill: #61dafb; -fx-font-weight: bold; -fx-font-size: 14px;");

        Button btnTorna = new Button("⬅ Torna alle Attività");
        btnTorna.setOnAction(e -> root.setCenter(creaVistaDettaglioProgetto(progettoSelezionato, root, listaProgettiHome)));

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

        VBox box = new VBox(15, btnTorna, lblTitolo, pieChart, new Label("Dettaglio Attivita per Stato:"), txtDettagli);
        box.setPadding(new Insets(15));

        ScrollPane scrollPane = new ScrollPane(box);
        scrollPane.setFitToWidth(true);
        return scrollPane;
    }

    private VBox creaVistaReportGlobale() {
        Label lbl = new Label("📊 Report Globale di Tutti i Progetti");
        lbl.setStyle("-fx-text-fill: #61dafb; -fx-font-weight: bold; -fx-font-size: 14px;");
        StringBuilder reportText = new StringBuilder();
        List<Progetto> progettiUtente = progettoController.getProgettiUtente(utenteCorrente.getIdUtente());
        for (Progetto p : progettiUtente) {
            reportText.append("📁 Progetto: ").append(p.getNome()).append("\n");
            List<Attivita> taskDelProgetto = attivitaController.getAttivitaByProgetto(p.getIdProgetto());
            if (taskDelProgetto.isEmpty()) {
                reportText.append("   (Nessuna attività presente)\n");
            } else {
                for (Attivita a : taskDelProgetto) {
                    reportText.append("   - ").append(a.getTitolo())
                            .append(" [").append(a.getStato()).append("]\n");
                }
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