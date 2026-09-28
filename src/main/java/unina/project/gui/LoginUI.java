package unina.project.gui;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class LoginUI extends Application {
    @Override
    public void start(Stage stage) {
        stage.setTitle("UninaTaskBoard - Login");

        TextField txtEmail = new TextField();
        txtEmail.setPromptText("Email (@unina.it)");

        PasswordField txtPassword = new PasswordField();
        txtPassword.setPromptText("Password");

        Button btnLogin = new Button("Accedi");
        Button btnRegistrati = new Button("Vai alla Registrazione");
        Label lblMsg = new Label();

        btnLogin.setOnAction(e -> {
            if (txtEmail.getText().isEmpty() || txtPassword.getText().isEmpty()) {
                lblMsg.setText("Compila tutti i campi.");
            } else {
                stage.close();
                // Passa l'email inserita alla dashboard per renderla dinamica
                new DashboardProgettiUI(txtEmail.getText()).start(new Stage());
            }
        });

        btnRegistrati.setOnAction(e -> {
            stage.close();
            new RegistrazioneUI().start(new Stage());
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