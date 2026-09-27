package unina.project.gui;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class LoginUI extends Application {

    private final AutenticazioneController authController = new AutenticazioneController();

    @Override
    public void start(Stage stage) {
        stage.setTitle("UninaTaskBoard - Accesso");

        TabPane tabPane = new TabPane();

        // --- TAB LOGIN ---
        VBox boxLogin = new VBox(10);
        boxLogin.setPadding(new Insets(15));
        TextField txtEmailLogin = new TextField();
        txtEmailLogin.setPromptText("Email (@unina.it)");
        PasswordField txtPassLogin = new PasswordField();
        txtPassLogin.setPromptText("Password");
        Button btnAccedi = new Button("Accedi");
        Label lblMsgLogin = new Label();

        btnAccedi.setOnAction(e -> {
            String email = txtEmailLogin.getText().trim();
            String password = txtPassLogin.getText();
            if (authController.effettuaLogin(email, password)) {
                stage.close();
                // Passa l'email alla dashboard per recuperare i dati reali
                new DashboardProgettiUI(email).start(new Stage());
            } else {
                lblMsgLogin.setStyle("-fx-text-fill: red;");
                lblMsgLogin.setText("Credenziali non valide o campi vuoti.");
            }
        });
        boxLogin.getChildren().addAll(new Label("Benvenuto, effettua il login"), txtEmailLogin, txtPassLogin, btnAccedi, lblMsgLogin);

        // --- TAB REGISTRAZIONE ---
        VBox boxReg = new VBox(10);
        boxReg.setPadding(new Insets(15));
        TextField txtNomeReg = new TextField();
        txtNomeReg.setPromptText("Nome e Cognome");
        TextField txtEmailReg = new TextField();
        txtEmailReg.setPromptText("Email (@unina.it)");
        PasswordField txtPassReg = new PasswordField();
        txtPassReg.setPromptText("Password");

        ChoiceBox<String> choiceRuolo = new ChoiceBox<>();
        choiceRuolo.getItems().addAll("Studente", "Docente");
        choiceRuolo.setValue("Studente");

        Button btnRegistra = new Button("Registrati");
        Label lblMsgReg = new Label();

        btnRegistra.setOnAction(e -> {
            String nome = txtNomeReg.getText().trim();
            String email = txtEmailReg.getText().trim();
            String pass = txtPassReg.getText();
            String ruolo = choiceRuolo.getValue();

            if (authController.registraUtente(nome, email, pass, ruolo)) {
                lblMsgReg.setStyle("-fx-text-fill: green;");
                lblMsgReg.setText("Registrazione completata! Ora puoi fare il login.");
            } else {
                lblMsgReg.setStyle("-fx-text-fill: red;");
                lblMsgReg.setText("Errore: usa un'email @unina.it valida.");
            }
        });
        boxReg.getChildren().addAll(new Label("Crea un nuovo account"), txtNomeReg, txtEmailReg, txtPassReg, new Label("Ruolo:"), choiceRuolo, btnRegistra, lblMsgReg);

        Tab tab1 = new Tab("Login", boxLogin);
        Tab tab2 = new Tab("Registrazione", boxReg);
        tab1.setClosable(false);
        tab2.setClosable(false);
        tabPane.getTabs().addAll(tab1, tab2);

        stage.setScene(new Scene(tabPane, 350, 340));
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}