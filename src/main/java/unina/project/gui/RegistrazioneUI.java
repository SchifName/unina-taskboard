package unina.project.gui;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import unina.project.controller.*;

public class RegistrazioneUI {

    private final AutenticazioneController authController;
    private final ProgettoController progettoController;
    private final AttivitaController attivitaController;
    private final FileController fileController;

    public RegistrazioneUI(AutenticazioneController authController, ProgettoController progettoController,
                           AttivitaController attivitaController, FileController fileController) {
        this.authController = authController;
        this.progettoController = progettoController;
        this.attivitaController = attivitaController;
        this.fileController = fileController;
    }

    public void start(Stage stage) {
        stage.setTitle("UninaTaskBoard - Registrazione");

        TextField txtNome = new TextField();
        txtNome.setPromptText("Nome");

        TextField txtCognome = new TextField();
        txtCognome.setPromptText("Cognome");

        TextField txtEmail = new TextField();
        txtEmail.setPromptText("Email (@studenti.unina.it)");

        PasswordField txtPassword = new PasswordField();
        txtPassword.setPromptText("Password");

        Button btnRegistra = new Button("Registrati");
        Button btnTorna = new Button("Torna al Login");
        Label lblMsg = new Label();

        btnRegistra.setOnAction(e -> {
            String nome = txtNome.getText().trim();
            String cognome = txtCognome.getText().trim();
            String email = txtEmail.getText().trim();
            String password = txtPassword.getText().trim();

            if (nome.isEmpty() || cognome.isEmpty() || email.isEmpty() || password.isEmpty()) {
                lblMsg.setStyle("-fx-text-fill: red;");
                lblMsg.setText("Compila tutti i campi.");
                return;
            }

            if (!email.contains("@studenti.unina.it")) {
                lblMsg.setStyle("-fx-text-fill: red;");
                lblMsg.setText("Inserisci un'email valida.");
                return;
            }

            RisultatoRegistrazione esitoRegistrazione = authController.registraUtente(email, password, nome, cognome);

            switch (esitoRegistrazione) {
                case SUCCESSO:
                    lblMsg.setStyle("-fx-text-fill: green;");
                    lblMsg.setText("Registrazione completata con successo");
                    break;

                case ERR_EMAIL:
                    lblMsg.setStyle("-fx-text-fill: red;");
                    lblMsg.setText("Attenzione: Questa email è già registrata.");
                    break;

                default:
                    lblMsg.setStyle("-fx-text-fill: red;");
                    lblMsg.setText("Errore durante la registrazione.");
                    break;
            }
        });

        btnTorna.setOnAction(e -> {
            stage.close();
            new LoginUI(authController, progettoController, attivitaController, fileController)
                    .start(new Stage());
        });

        VBox root = new VBox(10, new Label("Registrazione"), txtNome, txtCognome, txtEmail, txtPassword, btnRegistra, btnTorna, lblMsg);
        root.setPadding(new Insets(20));

        stage.setScene(new Scene(root, 300, 380));
        stage.show();
    }
}