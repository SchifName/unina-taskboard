package unina.project.gui;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class RegistrazioneUI {

    // Cambiato da start() a mostra() per evitare conflitti con Application
    public void mostra(Stage stage) {
        stage.setTitle("UninaTaskBoard - Registrazione");

        TextField txtNome = new TextField();
        txtNome.setPromptText("Nome");

        TextField txtEmail = new TextField();
        txtEmail.setPromptText("Email (@unina.it)");

        PasswordField txtPassword = new PasswordField();
        txtPassword.setPromptText("Password");

        Button btnRegistra = new Button("Registrati");
        Button btnTorna = new Button("Torna al Login");
        Label lblMsg = new Label();

        // Azione Registrazione
        btnRegistra.setOnAction(e -> {
            if (txtNome.getText().isEmpty() || txtEmail.getText().isEmpty() || txtPassword.getText().isEmpty()) {
                lblMsg.setText("Compila tutti i campi.");
            } else {
                lblMsg.setText("Registrazione completata!");
            }
        });

        // Torna alla schermata di login (qui richiamiamo il LoginUI esistente)
        btnTorna.setOnAction(e -> {
            LoginUI login = new LoginUI();
            try {
                login.start(new Stage());
                stage.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        VBox root = new VBox(10, new Label("Registrazione"), txtNome, txtEmail, txtPassword, btnRegistra, btnTorna, lblMsg);
        root.setPadding(new Insets(20));

        stage.setScene(new Scene(root, 300, 320));
        stage.show();
    }
}