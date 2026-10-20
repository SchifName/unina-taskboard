package unina.project.gui;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import unina.project.controller.AttivitaController;
import unina.project.controller.AutenticazioneController;
import unina.project.controller.FileController;
import unina.project.controller.ProgettoController;

public class LoginUI extends Application {

    private final AutenticazioneController authController;
    private final ProgettoController progettoController;
    private final AttivitaController attivitaController;
    private final FileController fileController;

    // Costruttore chiamato all'avvio da Application.launch()
    public LoginUI() {
        this.authController = new AutenticazioneController();
        this.progettoController = new ProgettoController(this.authController);
        this.attivitaController = new AttivitaController(this.authController, this.progettoController);
        this.fileController = new FileController(this.authController, this.attivitaController);
    }

    // Costruttore per restituire il controllo mantenendo le stesse istanze dei Controller
    public LoginUI(AutenticazioneController authController, ProgettoController progettoController,
                   AttivitaController attivitaController, FileController fileController) {
        this.authController = authController;
        this.progettoController = progettoController;
        this.attivitaController = attivitaController;
        this.fileController = fileController;
    }

    @Override
    public void start(Stage stage) {
        stage.setTitle("UninaTaskBoard - Login");

        TextField txtEmail = new TextField();
        txtEmail.setPromptText("Email (@unina.it)");

        PasswordField txtPassword = new PasswordField();
        txtPassword.setPromptText("Password");

        Button btnLogin = new Button("Accedi");
        Button btnRegistrati = new Button("Registrati");
        Label lblMsg = new Label();

        btnLogin.setOnAction(e -> {
            String email = txtEmail.getText().trim();
            String password = txtPassword.getText();

            if (email.isEmpty() || password.isEmpty()) {
                lblMsg.setText("Compila tutti i campi.");
            } else {
                boolean loginOk = authController.effettuaLogin(email, password);

                if (loginOk) {
                    stage.close();
                    new DashboardProgettiUI(authController, progettoController, attivitaController, fileController)
                            .start(new Stage());
                } else {
                    lblMsg.setText("Dati errati");
                }
            }
        });

        btnRegistrati.setOnAction(e -> {
            stage.close();
            new RegistrazioneUI(authController, progettoController, attivitaController, fileController)
                    .start(new Stage());
        });

        VBox root = new VBox(10, new Label("Accedi"), txtEmail, txtPassword, btnLogin, btnRegistrati, lblMsg);
        root.setPadding(new Insets(20));

        stage.setScene(new Scene(root, 300, 300));
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}