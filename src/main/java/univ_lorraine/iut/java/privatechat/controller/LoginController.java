package univ_lorraine.iut.java.privatechat.controller;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import univ_lorraine.iut.java.privatechat.App;

public class LoginController {

    @FXML
    TextField txtusername;
    @FXML
    TextField txtpassword;
    @FXML
    private void login() throws IOException {

        if (txtusername.getText().isEmpty()) {
            throw new IllegalArgumentException("Le champ username est vide");
        }
        if (txtusername.getText().length() < 6) {
            throw new IllegalArgumentException("Le username doit contenir au moins 6 caractères");
        }


        if (txtpassword.getText().isEmpty()) {
            throw new IllegalArgumentException("Le champ password est vide");
        }
        if (txtpassword.getText().length() < 8) {
            throw new IllegalArgumentException("Le password doit contenir au moins 8 caractères");
        }

        App.setRoot("chat");
    }




}
