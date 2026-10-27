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

public class DettaglioProgettoUI {

    private final ProgettoController progettoController;
    private final AttivitaController attivitaController;
    private final FileController fileController;

    public DettaglioProgettoUI(ProgettoController progettoController, AttivitaController attivitaController, FileController fileController) {
        this.progettoController = progettoController;
        this.attivitaController = attivitaController;
        this.fileController = fileController;
    }

    public VBox creaVistaDettaglio(BorderPane root, ListView<String> listaHome) {
        Label lblTitolo = new Label("Progetto: " + progettoController.getNomeProgettoSelezionato());
        lblTitolo.setStyle("-fx-text-fill: #61dafb; -fx-font-weight: bold; -fx-font-size: 14px;");

        Button btnIndietro = new Button("⬅ Torna ai Progetti");
        btnIndietro.setOnAction(e -> root.setCenter(listaHome.getParent()));

        ListView<String> listaTask = new ListView<>();
        listaTask.getItems().setAll(attivitaController.caricaAttivitaProgettoCorrente());

        Button btnNuova = new Button("+ Nuova Attività");
        Button btnInvita = new Button("Invita Membro");
        Button btnCarica = new Button("📤 Carica File");
        Button btnScarica = new Button("📥 Scarica Ultimo File");
        Button btnStorico = new Button("📜 Storico File");
        Button btnStats = new Button("📈 Grafico Statistiche Progetto");

        btnStorico.setOnAction(e -> {
            int idx = listaTask.getSelectionModel().getSelectedIndex();
            if (!attivitaController.selezionaAttivita(idx)) {
                new Alert(Alert.AlertType.WARNING, "Seleziona un'attività.", ButtonType.OK).showAndWait();
                return;
            }
            List<String> storico = fileController.getStoricoRevisioni();
            if (storico.isEmpty()) {
                new Alert(Alert.AlertType.INFORMATION, "Nessun file presente.", ButtonType.OK).showAndWait();
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

        btnNuova.setOnAction(e -> {
            Dialog<ButtonType> dlg = new Dialog<>();
            dlg.setTitle("Nuova Attività");
            TextField txtT = new TextField(); txtT.setPromptText("Titolo");
            TextField txtD = new TextField(); txtD.setPromptText("Descrizione");
            DatePicker dp = new DatePicker(); dp.setPrefWidth(300);
            TextField txtTipo = new TextField(); txtTipo.setPromptText("Tipo (Sviluppo / Documentazione / Studio)");
            ComboBox<String> cmb = new ComboBox<>();
            cmb.getItems().addAll("(Completata)", "(In Corso)", "(Non Iniziata)");
            cmb.setValue("(Non Iniziata)");

            dlg.getDialogPane().setContent(new VBox(10, new Label("Titolo:"), txtT, new Label("Descrizione:"), txtD, new Label("Scadenza:"), dp, new Label("Tipo:"), txtTipo, new Label("Stato:"), cmb));
            dlg.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

            dlg.showAndWait().ifPresent(res -> {
                if (res == ButtonType.OK && !txtT.getText().isBlank()) {
                    if (attivitaController.inserisciAttivita(txtT.getText(), txtD.getText(), dp.getValue(), txtTipo.getText(), cmb.getValue())) {
                        listaTask.getItems().setAll(attivitaController.caricaAttivitaProgettoCorrente());
                    } else {
                        new Alert(Alert.AlertType.ERROR, "Errore salvataggio.", ButtonType.OK).showAndWait();
                    }
                }
            });
        });

        btnScarica.setOnAction(e -> {
            int idx = listaTask.getSelectionModel().getSelectedIndex();
            if (!attivitaController.selezionaAttivita(idx)) {
                new Alert(Alert.AlertType.WARNING, "Seleziona un'attività.", ButtonType.OK).showAndWait();
                return;
            }
            FileChooser fcDialog = new FileChooser();
            fcDialog.setInitialFileName(fileController.getNomeFileOriginale());
            File dest = fcDialog.showSaveDialog(root.getScene().getWindow());
            if (dest != null && fileController.scaricaUltimaRevisione(dest)) {
                new Alert(Alert.AlertType.INFORMATION, "Scaricato con successo.", ButtonType.OK).showAndWait();
            }
        });

        btnCarica.setOnAction(e -> {
            int idx = listaTask.getSelectionModel().getSelectedIndex();
            if (!attivitaController.selezionaAttivita(idx)) {
                new Alert(Alert.AlertType.WARNING, "Seleziona un'attività.", ButtonType.OK).showAndWait();
                return;
            }
            File scelto = new FileChooser().showOpenDialog(root.getScene().getWindow());
            if (scelto != null) {
                new TextInputDialog("Prima versione").showAndWait().ifPresent(nota -> {
                    if (fileController.salvaFileERevisione(scelto, nota)) {
                        new Alert(Alert.AlertType.INFORMATION, "File salvato!", ButtonType.OK).showAndWait();
                    }
                });
            }
        });

        btnStats.setOnAction(e -> {
            StatisticheProgettoUI statsUI = new StatisticheProgettoUI(progettoController, attivitaController, fileController);
            root.setCenter(statsUI.creaVistaStatistiche(root, listaHome));
        });

        btnInvita.setOnAction(event -> {
            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("Invita Membro");
            dialog.setHeaderText("Aggiungi un collaboratore al progetto");
            dialog.setContentText("Inserisci l'email dell'utente registrato:");

            dialog.showAndWait().ifPresent(email -> {
                if (!email.isBlank()) {
                    // Deleghiamo l'operazione al Controller
                    unina.project.controller.EsitoInvito esito = progettoController.invitaMembro(email);

                    // Gestiamo i vari casi con dei pop-up di avviso
                    switch (esito) {
                        case SUCCESSO:
                            new Alert(Alert.AlertType.INFORMATION, "Utente aggiunto con successo al progetto!", ButtonType.OK).showAndWait();
                            break;
                        case UTENTE_NON_TROVATO:
                            new Alert(Alert.AlertType.WARNING, "Nessun utente trovato con questa email.", ButtonType.OK).showAndWait();
                            break;
                        case GIA_MEMBRO:
                            new Alert(Alert.AlertType.INFORMATION, "L'utente fa già parte di questo progetto.", ButtonType.OK).showAndWait();
                            break;
                        default:
                            new Alert(Alert.AlertType.ERROR, "Errore durante l'invito. Riprova.", ButtonType.OK).showAndWait();
                            break;
                    }
                }
            });
        });

        VBox box = new VBox(12, btnIndietro, lblTitolo, new Label("Elenco Attività:"), listaTask, new HBox(10, btnNuova,btnInvita, btnScarica, btnCarica, btnStorico, btnStats));
        box.setPadding(new Insets(15));
        return box;
    }
}