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
            if (txtNome.getText().isEmpty() || txtEmail.getText().isEmpty() || txtPassword.getText().isEmpty()) {
                lblMsg.setText("Compila tutti i campi.");
            } else {
                lblMsg.setText("Registrazione completata!");
            }
        });

        btnTorna.setOnAction(e -> {
            stage.close();
            new LoginUI().start(new Stage());
        });

        VBox root = new VBox(10, new Label("Registrazione"), txtNome, txtEmail, txtPassword, boxRuolo, btnRegistra, btnTorna, lblMsg);
        root.setPadding(new Insets(20));

        stage.setScene(new Scene(root, 300, 360));
        stage.show();
    }
}