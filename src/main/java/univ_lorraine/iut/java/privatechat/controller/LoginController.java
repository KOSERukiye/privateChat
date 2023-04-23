package univ_lorraine.iut.java.privatechat.controller;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import univ_lorraine.iut.java.privatechat.App;

public class LoginController {



  @FXML
private void handleLoginButton(ActionEvent event) {
    boolean isValid = true;
    String username = txtUsername.getText();
    String password = txtPassword.getText();
    
    if (username.isEmpty() || password.isEmpty() || username.length() < 6) {
        isValid = false;
        if (username.isEmpty()) {
            txtUsername.setStyle("-fx-border-color: red;");
        } else {
            txtUsername.setStyle("");
        }
        if (password.isEmpty()) {
            txtPassword.setStyle("-fx-border-color: red;");
        } else {
            txtPassword.setStyle("");
        }
    } else {
        try (Scanner scanner = new Scanner(new File("users.txt"))) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (line.equals(username)) {
                    isValid = true;
                    break;
                }
            }
        } catch (FileNotFoundException ex) {
            System.err.println("Fichier des utilisateurs introuvable.");
            isValid = false;
        }
    }
    
    if (isValid) {
        // Connexion réussie
    } else {
        // Erreur de connexion
    }
}
}

