package unina.project.gui;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class DashboardProgettiUI {
    private final String nome, ruolo;
    private boolean isDark;

    // Costruttore per ricevere i dati dell'utente loggato
    public DashboardProgettiUI(String nome, String ruolo) {
        this.nome = nome;
        this.ruolo = ruolo;
    }

    public void start(Stage stage) {
        stage.setTitle("Dashboard - " + ruolo);
        BorderPane root = new BorderPane();

        // ==========================================
        // 1. SEZIONE GESTIONE PROGETTI
        // ==========================================
        ListView<String> lista = new ListView<>();
        lista.getItems().addAll("Progetto 1", "Progetto 2");

        Button btnCrea = new Button("Crea"), btnMod = new Button("Modifica"), btnDel = new Button("Elimina");

        // Azione per creare un nuovo progetto
        btnCrea.setOnAction(e -> new TextInputDialog().showAndWait().ifPresent(s -> {
            if (!s.isBlank()) lista.getItems().add(s.trim());
        }));

        // Azione per modificare il progetto selezionato
        btnMod.setOnAction(e -> {
            String sel = lista.getSelectionModel().getSelectedItem();
            if (sel != null) {
                new TextInputDialog(sel).showAndWait().ifPresent(s -> {
                    if (!s.isBlank()) lista.getItems().set(lista.getSelectionModel().getSelectedIndex(), s.trim());
                });
            } else new Alert(Alert.AlertType.WARNING, "Seleziona prima un progetto.").show();
        });

        // Azione per eliminare il progetto selezionato
        btnDel.setOnAction(e -> {
            String sel = lista.getSelectionModel().getSelectedItem();
            if (sel != null) lista.getItems().remove(sel);
            else new Alert(Alert.AlertType.WARNING, "Seleziona prima un progetto.").show();
        });

        VBox boxProj = new VBox(10, new Label("Gestione Progetti"), new HBox(10, btnCrea, btnMod, btnDel), lista);
        boxProj.setPadding(new Insets(10));

        // ==========================================
        // 2. SEZIONE PROFILO UTENTE
        // ==========================================
        VBox boxProf = new VBox(10, new Label("Profilo"), new Label("Nome: " + nome), new Label("Ruolo: " + ruolo));
        boxProf.setPadding(new Insets(10));

        // ==========================================
        // 3. SEZIONE IMPOSTAZIONI E TEMI
        // ==========================================
        CheckBox chkDark = new CheckBox("Modalità Oscura");
        Slider slider = new Slider(10, 20, 12);
        ChoiceBox<String> tema = new ChoiceBox<>();
        tema.getItems().addAll("Standard", "Blu Accento", "Verde Natura");
        tema.setValue("Standard");

        // Logica dinamica per aggiornare i colori e la dimensione del testo
        Runnable aggiorna = () -> {
            isDark = chkDark.isSelected();
            String bg = isDark ? "-fx-background-color: #222; -fx-text-fill: #61dafb;" : "-fx-background-color: #f4f4f4;";
            if ("Blu Accento".equals(tema.getValue())) bg = isDark ? "-fx-background-color: #1a2536; -fx-text-fill: #61dafb;" : "-fx-background-color: #e3f2fd;";
            else if ("Verde Natura".equals(tema.getValue())) bg = isDark ? "-fx-background-color: #1b3022; -fx-text-fill: #81c784;" : "-fx-background-color: #e8f5e9;";

            String style = bg + " -fx-font-size: " + slider.getValue() + "px;";
            root.setStyle(style); boxProj.setStyle(style); boxProf.setStyle(style);
        };

        chkDark.setOnAction(e -> aggiorna.run());
        slider.valueProperty().addListener(e -> aggiorna.run());
        tema.setOnAction(e -> aggiorna.run());

        VBox boxSet = new VBox(10, new Label("Impostazioni"), chkDark, new CheckBox("Notifiche Push"), new Label("Dimensione Testo:"), slider, new Label("Tema:"), tema);
        boxSet.setPadding(new Insets(10));

        // ==========================================
        // 4. MENU LATERALE (SIDEBAR)
        // ==========================================
        // Stile per mantenere le scritte celesti fisse e senza sbiadire
        String sStyle = "-fx-background-color: transparent; -fx-text-fill: #61dafb; -fx-cursor: hand; -fx-font-weight: bold; -fx-alignment: CENTER_LEFT;";
        Button bP = new Button("Progetti"), bProf = new Button("Profilo"), bSet = new Button("Impostazioni"), bEsc = new Button("Esci");
        for (Button b : new Button[]{bP, bProf, bSet, bEsc}) b.setStyle(sStyle);

        // Collegamento dei bottoni del menu alle varie schermate centrali
        bP.setOnAction(e -> root.setCenter(boxProj));
        bProf.setOnAction(e -> root.setCenter(boxProf));
        bSet.setOnAction(e -> root.setCenter(boxSet));
        bEsc.setOnAction(e -> stage.close());

        VBox sidebar = new VBox(10, new Label("MENU"), bP, bProf, bSet, new Separator(), bEsc);
        sidebar.setPadding(new Insets(10));
        sidebar.setStyle("-fx-background-color: #1a1a1a;");

        // Assemblaggio finale del layout principale
        root.setLeft(sidebar);
        root.setCenter(boxProj);
        stage.setScene(new Scene(root, 650, 400));
        stage.show();
    }
}