package unina.project.gui;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.util.List;

public class DashboardProgettiUI {

    private final AutenticazioneController controller = new AutenticazioneController();
    private final String emailUtente;
    private final String nomeUtente;
    private final String ruoloUtente;

    public DashboardProgettiUI(String emailUtente) {
        this.emailUtente = emailUtente;
        String[] info = controller.getInfoUtente(emailUtente);
        this.nomeUtente = info[0];
        this.ruoloUtente = info[1];
    }

    public void start(Stage stage) {
        stage.setTitle("UninaTaskBoard - Dashboard (" + ruoloUtente + ")");
        BorderPane root = new BorderPane();

        // Stili per il testo celeste
        String stileCeleste = "-fx-text-fill: #61dafb;";
        String stileTitolo = "-fx-text-fill: #61dafb; -fx-font-weight: bold; -fx-font-size: 14px;";

        // 1. PANNELLO PROGETTI
        ListView<String> listaProgetti = new ListView<>();
        aggiornaListaProgetti(listaProgetti);

        Button btnNuovo = new Button("+ Nuovo Progetto");
        Button btnModifica = new Button("✏️ Modifica");
        Button btnElimina = new Button("🗑 Elimina");

        btnNuovo.setOnAction(e -> {
            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("Nuovo Progetto");
            dialog.setHeaderText("Crea un nuovo progetto collaborativo");
            dialog.setContentText("Nome del progetto:");

            dialog.showAndWait().ifPresent(nome -> {
                if (!nome.trim().isEmpty()) {
                    controller.creaNuovoProgetto(nome);
                    aggiornaListaProgetti(listaProgetti);
                }
            });
        });

        btnModifica.setOnAction(e -> {
            String selezionato = listaProgetti.getSelectionModel().getSelectedItem();
            if (selezionato != null) {
                TextInputDialog dialog = new TextInputDialog(selezionato);
                dialog.setTitle("Modifica Progetto");
                dialog.setHeaderText("Modifica il nome del progetto");
                dialog.setContentText("Nuovo nome:");

                dialog.showAndWait().ifPresent(nuovoNome -> {
                    if (!nuovoNome.trim().isEmpty()) {
                        int index = listaProgetti.getSelectionModel().getSelectedIndex();
                        listaProgetti.getItems().set(index, nuovoNome.trim());
                    }
                });
            } else {
                new Alert(Alert.AlertType.WARNING, "Seleziona prima un progetto da modificare.", ButtonType.OK).showAndWait();
            }
        });

        btnElimina.setOnAction(e -> {
            String sel = listaProgetti.getSelectionModel().getSelectedItem();
            if (sel != null) {
                listaProgetti.getItems().remove(sel);
            } else {
                new Alert(Alert.AlertType.WARNING, "Seleziona prima un progetto da eliminare.", ButtonType.OK).showAndWait();
            }
        });

        HBox boxBottoni = new HBox(10, btnNuovo, btnModifica, btnElimina);
        Label lblTitoloProj = new Label("I Miei Progetti:");
        lblTitoloProj.setStyle(stileTitolo);
        VBox boxProgetti = new VBox(10, lblTitoloProj, boxBottoni, listaProgetti);
        boxProgetti.setPadding(new Insets(15));

        // 2. PANNELLO PROFILO (Tutte le scritte in celeste)
        Label lblProfTitolo = new Label("Informazioni Profilo Utente");
        lblProfTitolo.setStyle(stileTitolo);
        Label lblNome = new Label("Nome: " + nomeUtente);
        Label lblRuolo = new Label("Ruolo: " + ruoloUtente);
        Label lblEmail = new Label("Email: " + emailUtente);
        lblNome.setStyle(stileCeleste);
        lblRuolo.setStyle(stileCeleste);
        lblEmail.setStyle(stileCeleste);

        VBox boxProfilo = new VBox(10, lblProfTitolo, lblNome, lblRuolo, lblEmail);
        boxProfilo.setPadding(new Insets(15));

        // 3. PANNELLO IMPOSTAZIONI (Tutte le scritte e checkbox in celeste)
        Label lblSetTitolo = new Label("Impostazioni di Sistema");
        lblSetTitolo.setStyle(stileTitolo);

        CheckBox chkDark = new CheckBox("Modalità Oscura");
        CheckBox chkNotifiche = new CheckBox("Notifiche Push Attive");
        chkNotifiche.setSelected(true);

        // Forziamo il colore del testo delle CheckBox in celeste
        chkDark.setStyle(stileCeleste);
        chkNotifiche.setStyle(stileCeleste);

        Label lblZoom = new Label("Barra di Ingrandimento (Zoom Testo):");
        lblZoom.setStyle(stileCeleste);
        Slider sliderZoom = new Slider(10, 24, 12);
        sliderZoom.setShowTickLabels(true);
        sliderZoom.setShowTickMarks(true);

        Runnable aggiornaStile = () -> {
            double fontSize = sliderZoom.getValue();
            boolean isDark = chkDark.isSelected();
            String bgColor = isDark ? "-fx-background-color: #2b2b2b;" : "-fx-background-color: #ffffff;";
            root.setStyle(bgColor + " -fx-font-size: " + fontSize + "px;");
        };

        chkDark.setOnAction(e -> aggiornaStile.run());
        sliderZoom.valueProperty().addListener((observable, oldValue, newValue) -> aggiornaStile.run());

        VBox boxImpostazioni = new VBox(10, lblSetTitolo, chkDark, chkNotifiche, lblZoom, sliderZoom);
        boxImpostazioni.setPadding(new Insets(15));

        // 4. MENU LATERALE (SIDEBAR)
        String stileMenu = "-fx-background-color: transparent; -fx-text-fill: #61dafb; -fx-cursor: hand; -fx-font-weight: bold; -fx-alignment: CENTER_LEFT; -fx-padding: 5;";

        Button btnP = new Button("📁 Progetti");
        Button btnProf = new Button("👤 Profilo");
        Button btnSet = new Button("⚙️ Impostazioni");
        Button btnEsc = new Button("🚪 Esci");

        btnP.setStyle(stileMenu);
        btnProf.setStyle(stileMenu);
        btnSet.setStyle(stileMenu);
        btnEsc.setStyle(stileMenu);

        btnP.setOnAction(e -> root.setCenter(boxProgetti));
        btnProf.setOnAction(e -> root.setCenter(boxProfilo));
        btnSet.setOnAction(e -> root.setCenter(boxImpostazioni));
        btnEsc.setOnAction(e -> {
            stage.close();
            new LoginUI().start(new Stage());
        });

        Label lblMenu = new Label("MENU");
        lblMenu.setStyle("-fx-text-fill: #61dafb; -fx-font-weight: bold;");
        VBox sidebar = new VBox(12, lblMenu, new Separator(), btnP, btnProf, btnSet, new Separator(), btnEsc);
        sidebar.setPadding(new Insets(15));
        sidebar.setStyle("-fx-background-color: #1a1a1a;");

        root.setLeft(sidebar);
        root.setCenter(boxProgetti);

        stage.setScene(new Scene(root, 780, 450));
        stage.show();
    }

    private void aggiornaListaProgetti(ListView<String> lista) {
        lista.getItems().clear();
        List<String> progettiDB = controller.getNomiProgettiUtente();
        lista.getItems().addAll(progettiDB);
    }
}