package unina.project.gui;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class RegistrazioneUI extends Application {

    @Override
    public void start(Stage stage) {
        stage.setTitle("UninaTaskBoard - Registrazione");

        VBox root = new VBox(12);
        root.setPadding(new Insets(25));

        Label titolo = new Label("Crea un Nuovo Account");
        titolo.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        TextField txtNome = new TextField();
        txtNome.setPromptText("Nome e Cognome");

        TextField txtEmail = new TextField();
        txtEmail.setPromptText("Email (@unina.it)");

        PasswordField txtPassword = new PasswordField();
        txtPassword.setPromptText("Password");

        // Scelta del ruolo separata nel form di registrazione
        ChoiceBox<String> choiceRuolo = new ChoiceBox<>();
        choiceRuolo.getItems().addAll("Studente", "Docente");
        choiceRuolo.setValue("Studente");

        Button btnRegistra = new Button("Registrati");
        Button btnTornaLogin = new Button("Torna al Login");
        Label lblMessaggio = new Label();

        // Azione Registrazione
        btnRegistra.setOnAction(e -> {
            String nome = txtNome.getText().trim();
            String email = txtEmail.getText().trim();
            String password = txtPassword.getText();
            String ruolo = choiceRuolo.getValue();

            if (nome.isEmpty() || email.isEmpty() || password.isEmpty()) {
                lblMessaggio.setStyle("-fx-text-fill: red;");
                lblMessaggio.setText("Compila tutti i campi obbligatori.");
                return;
            }

            // Registrazione completata con successo
            lblMessaggio.setStyle("-fx-text-fill: green;");
            lblMessaggio.setText("Registrazione completata come " + ruolo + "!");
        });

        // Pulsante per tornare indietro al Login
        btnTornaLogin.setOnAction(e -> {
            stage.close(); // Chiude la registrazione
            new LoginUI().start(new Stage()); // Riapre il login
        });

        root.getChildren().addAll(
                titolo, txtNome, txtEmail, txtPassword,
                new Label("Seleziona Ruolo:"), choiceRuolo,
                btnRegistra, btnTornaLogin, lblMessaggio
        );

        stage.setScene(new Scene(root, 340, 440));
        stage.show();
    }
}