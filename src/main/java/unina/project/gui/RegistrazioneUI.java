package unina.project.gui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
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

        Scene scene;

        Label lblTitolo = new Label("REGISTRATI");
        lblTitolo.getStyleClass().add("titolo");

        TextField txtNome = new TextField();
        txtNome.setPromptText("Nome");
        txtNome.getStyleClass().add("testo");

        TextField txtCognome = new TextField();
        txtCognome.setPromptText("Cognome");
        txtCognome.getStyleClass().add("testo");

        TextField txtEmail = new TextField();
        txtEmail.setPromptText("Email (@studenti.unina.it)");
        txtEmail.getStyleClass().add("testo");

        PasswordField txtPassword = new PasswordField();
        txtPassword.setPromptText("Password");
        txtPassword.getStyleClass().add("testo");

        Button btnRegistra = new Button("Registrati");
        btnRegistra.getStyleClass().add("bottoni");
        Button btnTorna = new Button("Torna al Login");
        btnTorna.getStyleClass().add("bottoni");

        Alert errore = new Alert(Alert.AlertType.ERROR);
        errore.setTitle("Errore di registrazione");
        errore.setHeaderText("");

        Alert successo = new Alert(Alert.AlertType.INFORMATION);
        successo.setTitle("Successo");
        successo.setHeaderText("");

        HBox btns;
        VBox logup;
        StackPane root;

        btnRegistra.setOnAction(e -> {
            String nome = txtNome.getText().trim();
            String cognome = txtCognome.getText().trim();
            String email = txtEmail.getText().trim();
            String password = txtPassword.getText().trim();

            if (nome.isEmpty() || cognome.isEmpty() || email.isEmpty() || password.isEmpty()) {
                errore.setContentText("Compila tutti i campi");
                errore.showAndWait();
                return;
            }

            if (!email.contains("@studenti.unina.it")) {
                errore.setContentText("Inserisci l'email universitaria");
                errore.showAndWait();
                return;
            }

            RisultatoRegistrazione esitoRegistrazione = authController.registraUtente(email, password, nome, cognome);

            switch (esitoRegistrazione) {
                case SUCCESSO:
                    successo.setContentText("Registrazione completata verrai reindirizzato alla pagina di LOGIN");
                    successo.showAndWait();
                    stage.close();
                    new LoginUI(authController, progettoController, attivitaController, fileController)
                            .start(new Stage());
                    break;

                case ERR_EMAIL:
                    errore.setContentText("Attenzione email già in uso");
                    errore.showAndWait();
                    break;

                default:
                    errore.setContentText("Errore durante la registrazione");
                    errore.showAndWait();
                    break;
            }
        });

        btnTorna.setOnAction(e -> {
            stage.close();
            new LoginUI(authController, progettoController, attivitaController, fileController)
                    .start(new Stage());
        });

        btns = new HBox(20, btnRegistra, btnTorna);
        btns.setAlignment(Pos.CENTER);

        logup = new VBox(20, lblTitolo, txtNome, txtCognome, txtEmail, txtPassword, btns);
        logup.setAlignment(Pos.CENTER);
        logup.setMaxHeight(250);
        logup.setMaxWidth(350);
        logup.setPadding(new Insets(30));
        logup.getStyleClass().add("card");

        root = new StackPane(logup);
        root.getStyleClass().add("sfondo");

        scene = new Scene(root, 800, 500);
        scene.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());
        stage.setScene(scene);
        stage.show();
    }
}