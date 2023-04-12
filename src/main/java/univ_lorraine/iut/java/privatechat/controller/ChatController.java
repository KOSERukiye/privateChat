package univ_lorraine.iut.java.privatechat.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import univ_lorraine.iut.java.privatechat.App;

public class ChatController {

    @FXML
    Button btnEnvoyer;
    @FXML
    TextField txtmessage;

    @FXML
    private void envoyer() throws IOException {
        // Vérifier le contenu du champ txtmessage
        String message = getAndValidateTextAreaContent();

        // Envoyer le message via une connexion réseau (par exemple)
        Socket socket = new Socket("localhost", 8080); // Remplacer par les informations de connexion nécessaires
        OutputStream outputStream = socket.getOutputStream();
        PrintWriter writer = new PrintWriter(outputStream);
        writer.println(message);
        writer.flush();
        writer.close();
        socket.close();

        // Effacer le contenu du champ txtmessage après l'envoi du message
        txtmessage.clear();
    }

    private String getAndValidateTextAreaContent() {
        String message = txtmessage.getText().trim();
        if (message.isEmpty()) {
            throw new IllegalArgumentException("Le champ 'Message' ne peut pas être vide.");
        }
        return message;
    }


    @FXML
    private void logout() throws IOException {

        App.setRoot("login");
    }
}