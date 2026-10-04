package unina.project.gui;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import unina.project.controller.AttivitaController;
import unina.project.controller.FileController;
import unina.project.entity.Attivita;
import unina.project.entity.Progetto;
import unina.project.entity.Utente;

import java.io.File;
import java.util.List;

public class DettaglioProgettoUI {
    private final Progetto progetto;
    private final Utente utente;
    private final AttivitaController attCtrl = new AttivitaController();

    public DettaglioProgettoUI(Progetto progetto, Utente utente) {
        this.progetto = progetto;
        this.utente = utente;
    }

    public VBox creaVistaDettaglio(BorderPane root, ListView<Progetto> listaHome) {
        Label lblTitolo = new Label("Progetto: " + progetto.getNome());
        lblTitolo.setStyle("-fx-text-fill: #61dafb; -fx-font-weight: bold; -fx-font-size: 14px;");

        Button btnIndietro = new Button("⬅ Torna ai Progetti");
        btnIndietro.setOnAction(e -> root.setCenter(listaHome.getParent()));

        ListView<Attivita> listaTask = new ListView<>();
        listaTask.getItems().addAll(attCtrl.getAttivitaByProgetto(progetto.getIdProgetto()));

        Button btnNuova = new Button("+ Nuova Attività");
        Button btnCarica = new Button("📤 Carica File");
        Button btnScarica = new Button("📥 Scarica Ultimo File");
        Button btnStorico = new Button("📜 Storico File");
        Button btnStats = new Button("📈 Grafico Statistiche Progetto");

        btnStorico.setOnAction(e -> {
            Attivita sel = listaTask.getSelectionModel().getSelectedItem();
            if (sel == null) {
                new Alert(Alert.AlertType.WARNING, "Seleziona un'attività.", ButtonType.OK).showAndWait();
                return;
            }
            List<String> storico = new FileController().getStoricoRevisioni(sel.getIdAttivita());
            if (storico.isEmpty()) {
                new Alert(Alert.AlertType.INFORMATION, "Nessun file presente.", ButtonType.OK).showAndWait();
                return;
            }
            Dialog<Void> dlg = new Dialog<>();
            dlg.setTitle("Cronologia Revisioni");
            dlg.setHeaderText("Storico file di: " + sel.getTitolo());
            ListView<String> lv = new ListView<>();
            lv.getItems().addAll(storico);
            lv.setPrefSize(450, 300);
            dlg.getDialogPane().setContent(lv);
            dlg.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);
            dlg.showAndWait();
        });

        btnNuova.setOnAction(e -> {
            Dialog<ButtonType> dlg = new Dialog<>();
            dlg.setTitle("Nuova Attività");
            TextField txtT = new TextField(); txtT.setPromptText("Titolo");
            TextField txtD = new TextField(); txtD.setPromptText("Descrizione");
            DatePicker dp = new DatePicker(); dp.setPrefWidth(300);
            TextField txtTipo = new TextField(); txtTipo.setPromptText("Tipo");
            ComboBox<String> cmb = new ComboBox<>();
            cmb.getItems().addAll("(Completata)", "(In Corso)", "(Non Iniziata)");
            cmb.setValue("(Non Iniziata)");

            dlg.getDialogPane().setContent(new VBox(10, new Label("Titolo:"), txtT, new Label("Descrizione:"), txtD, new Label("Scadenza:"), dp, new Label("Tipo:"), txtTipo, new Label("Stato:"), cmb));
            dlg.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

            dlg.showAndWait().ifPresent(res -> {
                if (res == ButtonType.OK && !txtT.getText().isBlank()) {
                    String st = cmb.getValue().equals("(Completata)") ? "Finita" : cmb.getValue().equals("(In Corso)") ? "Presa_in_carico" : "Nuova";
                    if (attCtrl.inserisciAttivita(progetto.getIdProgetto(), txtT.getText().trim(), txtD.getText().trim(), dp.getValue(), txtTipo.getText().trim(), st, utente.getIdUtente())) {
                        listaTask.getItems().setAll(attCtrl.getAttivitaByProgetto(progetto.getIdProgetto()));
                    } else {
                        new Alert(Alert.AlertType.ERROR, "Errore salvataggio.", ButtonType.OK).showAndWait();
                    }
                }
            });
        });

        btnScarica.setOnAction(e -> {
            Attivita sel = listaTask.getSelectionModel().getSelectedItem();
            if (sel == null) {
                new Alert(Alert.AlertType.WARNING, "Seleziona un'attività.", ButtonType.OK).showAndWait();
                return;
            }
            FileController fc = new FileController();
            javafx.stage.FileChooser fcDialog = new javafx.stage.FileChooser();
            fcDialog.setInitialFileName(fc.getNomeFileOriginale(sel.getIdAttivita()));
            File dest = fcDialog.showSaveDialog(root.getScene().getWindow());
            if (dest != null && fc.scaricaUltimaRevisione(sel.getIdAttivita(), dest)) {
                new Alert(Alert.AlertType.INFORMATION, "Scaricato con successo.", ButtonType.OK).showAndWait();
            }
        });

        btnCarica.setOnAction(e -> {
            Attivita sel = listaTask.getSelectionModel().getSelectedItem();
            if (sel == null) {
                new Alert(Alert.AlertType.WARNING, "Seleziona un'attività.", ButtonType.OK).showAndWait();
                return;
            }
            File scelto = new javafx.stage.FileChooser().showOpenDialog(root.getScene().getWindow());
            if (scelto != null) {
                new TextInputDialog("Prima versione").showAndWait().ifPresent(nota -> {
                    if (new FileController().salvaFileERevisione(sel.getIdAttivita(), utente.getIdUtente(), scelto, nota)) {
                        new Alert(Alert.AlertType.INFORMATION, "File salvato!", ButtonType.OK).showAndWait();
                    }
                });
            }
        });

        btnStats.setOnAction(e -> root.setCenter(new StatisticheProgettoUI(progetto, utente).creaVistaStatistiche(root, listaHome)));

        VBox box = new VBox(12, btnIndietro, lblTitolo, new Label("Elenco Attività:"), listaTask, new HBox(10, btnNuova, btnScarica, btnCarica, btnStorico, btnStats));
        box.setPadding(new Insets(15));
        return box;
    }
}