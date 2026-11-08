package unina.project.gui;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
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

        Scene scene;

        Label lblTitolo = new Label("ACCEDI");
        lblTitolo.getStyleClass().add("titolo");

        TextField txtEmail = new TextField();
        txtEmail.setPromptText("Email (@studenti.unina.it)");
        txtEmail.getStyleClass().add("testo");

        PasswordField txtPassword = new PasswordField();
        txtPassword.setPromptText("Password");
        txtPassword.getStyleClass().add("testo");

        Button btnLogin = new Button("Accedi");
        btnLogin.getStyleClass().add("bottoni");
        Button btnRegistrati = new Button("Registrati");
        btnRegistrati.getStyleClass().add("bottoni");

        HBox btns;
        VBox login;
        StackPane root;

        Alert errore = new Alert(Alert.AlertType.ERROR);
        errore.setTitle("Errore di registrazione");
        errore.setHeaderText("");


        btnLogin.setOnAction(e -> {
            String email = txtEmail.getText().trim();
            String password = txtPassword.getText();

            if (email.isEmpty() || password.isEmpty()) {
                errore.setContentText("Compila tutti i campi");
                errore.showAndWait();
            } else {
                boolean loginOk = authController.effettuaLogin(email, password);

                if (loginOk) {
                    stage.close();
                    new DashboardProgettiUI(authController, progettoController, attivitaController, fileController)
                            .start(new Stage());
                } else {
                    errore.setContentText("Email o password errati");
                    errore.showAndWait();
                    txtPassword.clear();
                }
            }
        });

        btnRegistrati.setOnAction(e -> {
            stage.close();
            new RegistrazioneUI(authController, progettoController, attivitaController, fileController)
                    .start(new Stage());
        });


        btns = new HBox(15,btnLogin, btnRegistrati);
        btns.setAlignment(Pos.CENTER);

        login = new VBox(20, lblTitolo, txtEmail, txtPassword, btns);
        login.setAlignment(Pos.CENTER);
        login.setMaxWidth(350);
        login.setMaxHeight(250);
        login.setPadding(new Insets(30));
        login.getStyleClass().add("card");

        root = new StackPane(login);
        root.getStyleClass().add("sfondo");

        scene = new Scene(root, 800, 500);
        scene.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}