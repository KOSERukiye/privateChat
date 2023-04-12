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
        boolean isValidUser = true;
        boolean isValidPassword = true;
        if (txtusername.getText().isEmpty() || txtusername.getText().length() < 6) {
            txtusername.setStyle("-fx-border-color: red ; -fx-border-width: 2px ; -fx-border-radius: 5px ;");
            isValidUser = false;
        }
        if (txtpassword.getText().isEmpty() || txtpassword.getText().length() < 8) {
            txtpassword.setStyle("-fx-border-color: red ; -fx-border-width: 2px ; -fx-border-radius: 5px ;");
            isValidPassword = false;
        }

        if(isValidUser && isValidPassword){
            App.setRoot("chat");
        }


    }







}
