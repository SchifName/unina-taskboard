package unina.project.gui;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import unina.project.controller.AutenticazioneController;
import unina.project.entity.Utente;

public class LoginUI extends Application {

    private final AutenticazioneController autenticazione = new AutenticazioneController();

    @Override
    public void start(Stage stage) {
        stage.setTitle("UninaTaskBoard - Login");

        TextField txtEmail = new TextField();
        txtEmail.setPromptText("Email (@unina.it)");

        PasswordField txtPassword = new PasswordField();
        txtPassword.setPromptText("Password");

        Button btnLogin = new Button("Accedi");

        // NUOVO PULSANTE
        Button btnRegistrati = new Button("Registrati");

        Label lblMsg = new Label();

        btnLogin.setOnAction(e -> {
            String email = txtEmail.getText().trim();
            String password = txtPassword.getText();

            if (email.isEmpty() || password.isEmpty()) {
                lblMsg.setText("Compila tutti i campi.");
            } else {
                Utente utenteLoggato = autenticazione.effettuaLogin(email, password);

                if (utenteLoggato != null) {
                    stage.close();
                    new DashboardProgettiUI(utenteLoggato).start(new Stage());
                } else {
                    lblMsg.setText("Dati errati");
                }
            }
        });

        // AZIONE DEL PULSANTE REGISTRATI
        btnRegistrati.setOnAction(e -> {
            stage.close();
            new RegistrazioneUI().start(new Stage());
        });

        // AGGIUNTO btnRegistrati AL VBox
        VBox root = new VBox(
                10,
                new Label("Accedi"),
                txtEmail,
                txtPassword,
                btnLogin,
                btnRegistrati,
                lblMsg
        );

        root.setPadding(new Insets(20));

        stage.setScene(new Scene(root, 300, 300));
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}