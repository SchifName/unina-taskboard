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
        stage.setTitle("UninaTaskBoard - Accesso");

        VBox root = new VBox(12);
        root.setPadding(new Insets(25));

        Label titolo = new Label("Accedi a UninaTaskBoard");
        titolo.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        TextField txtEmail = new TextField();
        txtEmail.setPromptText("Email istituzionale (@unina.it)");

        PasswordField txtPassword = new PasswordField();
        txtPassword.setPromptText("Password");

        Button btnAccedi = new Button("Accedi");
        Button btnVaiRegistrazione = new Button("Non hai un account? Registrati");
        Label lblMessaggio = new Label();

        // Azione Login
        btnAccedi.setOnAction(e -> {
            String email = txtEmail.getText().trim();
            String password = txtPassword.getText();

            if (email.isEmpty() || password.isEmpty()) {
                lblMessaggio.setText("Compila tutti i campi!");
                return;
            }

            // Simulazione login riuscito -> apre la Dashboard
            stage.close();
            new DashboardProgettiUI("Mario Rossi", "Studente").start(new Stage());
        });

        // Pulsante per aprire la schermata di Registrazione separata
        btnVaiRegistrazione.setOnAction(e -> {
            stage.close(); // Chiude il login
            new RegistrazioneUI().start(new Stage()); // Apre la registrazione
        });

        root.getChildren().addAll(titolo, txtEmail, txtPassword, btnAccedi, btnVaiRegistrazione, lblMessaggio);

        stage.setScene(new Scene(root, 320, 320));
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}