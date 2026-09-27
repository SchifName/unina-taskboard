package unina.project.gui;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class DashboardProgettiUI {
    private String nome, ruolo;
    private boolean isDark = false;

    public DashboardProgettiUI(String nome, String ruolo) {
        this.nome = nome;
        this.ruolo = ruolo;
    }

    public void start(Stage stage) {
        stage.setTitle("Dashboard - " + ruolo);
        BorderPane root = new BorderPane();

        // Sezione Progetti
        ListView<String> lista = new ListView<>();
        lista.getItems().addAll("Progetto 1", "Progetto 2");

        Button btnCreaProgetto = new Button("Crea Progetto");
        Button btnEliminaProgetto = new Button("Elimina Progetto");

        // Azione per creare un nuovo progetto inserendo il nome
        btnCreaProgetto.setOnAction(e -> {
            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("Nuovo Progetto");
            dialog.setHeaderText("Inserisci il nome del nuovo progetto:");
            dialog.setContentText("Nome:");

            dialog.showAndWait().ifPresent(nomeProgetto -> {
                if (!nomeProgetto.trim().isEmpty()) {
                    lista.getItems().add(nomeProgetto.trim());
                }
            });
        });

        // Azione per eliminare il progetto selezionato
        btnEliminaProgetto.setOnAction(e -> {
            String selezionato = lista.getSelectionModel().getSelectedItem();
            if (selezionato != null) {
                lista.getItems().remove(selezionato);
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING, "Seleziona prima un progetto da eliminare.");
                alert.showAndWait();
            }
        });

        HBox boxBottoni = new HBox(10, btnCreaProgetto, btnEliminaProgetto);
        VBox boxProj = new VBox(10, new Label("Gestione Progetti"), boxBottoni, lista);
        boxProj.setPadding(new Insets(10));

        // Sezione Profilo
        VBox boxProf = new VBox(10, new Label("Profilo"), new Label("Nome: " + nome), new Label("Ruolo: " + ruolo));
        boxProf.setPadding(new Insets(10));

        // Sezione Impostazioni & Dark Mode
        CheckBox chkDark = new CheckBox("Modalità Oscura");
        chkDark.setOnAction(e -> {
            isDark = chkDark.isSelected();
            String bg = isDark ? "-fx-background-color: #222; -fx-text-fill: white;" : "";
            root.setStyle(bg);
            boxProj.setStyle(bg);
            boxProf.setStyle(bg);
        });
        VBox boxSet = new VBox(10, new Label("Impostazioni"), chkDark);
        boxSet.setPadding(new Insets(10));

        // Menu Laterale (Sidebar)
        Hyperlink p = new Hyperlink("Progetti");
        Hyperlink prof = new Hyperlink("Profilo");
        Hyperlink set = new Hyperlink("Impostazioni");
        Hyperlink esc = new Hyperlink("Esci");

        p.setOnAction(e -> root.setCenter(boxProj));
        prof.setOnAction(e -> root.setCenter(boxProf));
        set.setOnAction(e -> root.setCenter(boxSet));
        esc.setOnAction(e -> stage.close());

        VBox sidebar = new VBox(10, new Label("MENU"), p, prof, set, new Separator(), esc);
        sidebar.setPadding(new Insets(10));

        root.setLeft(sidebar);
        root.setCenter(boxProj);
        stage.setScene(new Scene(root, 550, 320));
        stage.show();
    }
}