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

        TextField txtNome = new TextField();
        txtNome.setPromptText("Nome e Cognome");

        TextField txtEmail = new TextField();
        txtEmail.setPromptText("Email (@unina.it)");

        PasswordField txtPassword = new PasswordField();
        txtPassword.setPromptText("Password");

        ChoiceBox<String> boxRuolo = new ChoiceBox<>();
        boxRuolo.getItems().addAll("Studente", "Docente");
        boxRuolo.setValue("Studente");

        Button btnRegistra = new Button("Registrati");
        Button btnTorna = new Button("Torna al Login");
        Label lblMsg = new Label();

        btnRegistra.setOnAction(e -> {
            String nome = txtNome.getText().trim();
            String email = txtEmail.getText().trim();
            String password = txtPassword.getText().trim();

            // Controllo se i campi sono vuoti
            if (nome.isEmpty() || email.isEmpty() || password.isEmpty()) {
                lblMsg.setStyle("-fx-text-fill: red;");
                lblMsg.setText("Compila tutti i campi.");
                return;
            }

            // Controllo di validazione sull'email (es. deve contenere @)
            if (!email.contains("@")) {
                lblMsg.setStyle("-fx-text-fill: red;");
                lblMsg.setText("Inserisci un'email valida.");
                return;
            }

            // Simulazione / Verifica della registrazione andata a buon fine
            // (Qui puoi inserire la chiamata al tuo database o logica esistente)
            boolean registrazioneRiuscita = true;

            if (registrazioneRiuscita) {
                lblMsg.setStyle("-fx-text-fill: green;");
                lblMsg.setText("Registrazione completata con successo!");
            } else {
                lblMsg.setStyle("-fx-text-fill: red;");
                lblMsg.setText("Errore durante la registrazione.");
            }
        });

        btnTorna.setOnAction(e -> {
            stage.close();
            new LoginUI().start(new Stage());
        });

        VBox root = new VBox(10, new Label("Registrazione"), txtNome, txtEmail, txtPassword, boxRuolo, btnRegistra, btnTorna, lblMsg);
        root.setPadding(new Insets(20));

        stage.setScene(new Scene(root, 300, 380));
        stage.show();
    }
}