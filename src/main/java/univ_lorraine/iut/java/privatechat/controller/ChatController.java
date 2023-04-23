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

    
    
    //RECUP INFOS DE SESSIONS VIA UN FICHIER
@FXML
private void seConnecter() {
    try {
        Properties credentials = getCredentials();
        String login = credentials.getProperty("login");
        String password = credentials.getProperty("password");
        // Vérifier que les informations de connexion sont valides, etc.
        // ...
    } catch (IOException e) {
        // Gérer l'erreur de lecture du fichier de configuration
        // ...
    }
}

    //NEW CLASS POUR STOCKER INFOS D'UN USER
/*
public class Contact {
    private String username;
    private String ipAdd;
    private int port;

    
    //AFFICHIER LISTE USER DANS UN Viewqqchose javaFX
    /*
    @FXML
private ListView<String> listeContacts;

private List<String> contacts = new ArrayList<>(); // Liste des noms d'utilisateur

@FXML
public void initialize() {
    // Initialiser la liste des contacts avec des données fictives
    contacts.add("mimi.ma");
    contacts.add("leo");
    contacts.add("melanie");

    // Afficher les noms d'utilisateur dans la ListView
    listeContacts.setItems(FXCollections.observableArrayList(contacts));
}

//AJOUT NV CONTACT A LA LSITE DE CONTACTS
@FXML
private ListView<Contact> listeContacts;

private List<Contact> contacts = new ArrayList<>(); // Liste des contacts existants

@FXML
private void ajouterContact() {
    // Récupérer les informations du nouveau contact
    String username = "mimi.ma"; // Remplacer par le nom d'utilisateur saisi par l'utilisateur
    String ipAdd = "192.168.1.100"; // Remplacer par l'adresse IP saisie par l'utilisateur
    int port = 9876; // Remplacer par le port saisi par l'utilisateur

    // Créer un nouvel objet Contact pour le nouveau contact
    Contact newContact = new Contact(username, ipAdd, port);

    // Ajouter le nouvel objet Contact à la liste des contacts existants
    contacts.add(newContact);

    // Afficher les noms d'utilisateur des contacts dans la ListView
    listeContacts.setItems(FXCollections.observableArrayList(contacts));
}

@FXML
private ListView<Contact> listeContacts;
@FXML
private TextArea txtaffiche;

private List<Contact> contacts = new ArrayList<>(); // Liste des contacts existants

@FXML
private void initialize() {
    // Initialiser la ListView avec la liste des contacts existants
    listeContacts.setItems(FXCollections.observableArrayList(contacts));

    // Ajouter un listener pour détecter la sélection d'un contact dans la ListView
    listeContacts.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
        if (newValue != null) {
            // Afficher la liste des messages échangés avec le contact sélectionné
            txtaffiche.setText(String.join("\n", newValue.getMessages()));
        }
    });
}


//RECHERCHER UUN CONTACT VIA PSEUDO
@FXML
TextField champRecherche;
@FXML
ListView<Contact> listeContacts;

private List<Contact> contacts = new ArrayList<>(); // Liste des contacts existants

@FXML
private void recherche() {
    String recherche = champRecherche.getText().trim().toLowerCase();

    if (recherche.isEmpty()) {
        // Si le champ de recherche est vide, afficher tous les contacts existants
        listeContacts.setItems(FXCollections.observableArrayList(contacts));
    } else {
        // Sinon, afficher uniquement les contacts dont le pseudo contient la chaîne de recherche
        List<Contact> contactsTrouves = contacts.stream()
                .filter(contact -> contact.getPseudo().toLowerCase().contains(recherche))
                .collect(Collectors.toList());
        listeContacts.setItems(FXCollections.observableArrayList(contactsTrouves));
    }
}



@FXML
    private void envoyer() throws IOException {
        // Vérifier le contenu du champ txtmessage
        String message = getAndValidateTextAreaContent();

        // Envoyer le message via une connexion réseau (par exemple)
        InetAddress host = InetAddress.getLocalHost();
        Socket socket = new Socket(host.getHostName(), 9876);
        //Socket socket = new Socket("localhost", 8080); // Remplacer par les informations de connexion nécessaires
        OutputStream outputStream = socket.getOutputStream();
        PrintWriter writer = new PrintWriter(outputStream);
        writer.println(message);
        writer.flush();
        writer.close();
        socket.close();

        // Effacer le contenu du champ txtmessage après l'envoi du message
        txtmessage.clear();

        // Afficher le message envoyé
        afficheMSG(message);
        }

    private String getAndValidateTextAreaContent() {
        String message = txtmessage.getText().trim();
        if (message.isEmpty()) {
            throw new IllegalArgumentException("Le champ 'Message' ne peut pas être vide.");
        }
        return message;
    }
//faire un set message avec textaera pour que le mess de envoyer s'affiche sur l'écran txtaffiche
public void afficheMSG(String message) {
    // Afficher le message dans une zone de texte, une fenêtre de dialogue, ou autre
    System.out.println("Message envoyé : " + message);
}


//affichier anciens messages + tenter de se co là
    private void onContactClick(Contact contact) {
    // Vérifier si une connexion existe déjà pour ce contact
    if (!connexions.containsKey(contact)) {
        // Si la connexion n'existe pas, initialiser une nouvelle connexion
        try {
            Socket socket = new Socket(contact.getAdresse(), contact.getPort());
            Connexion connexion = new Connexion(socket);
            connexions.put(contact, connexion);

            // Afficher les anciennes conversations avec ce contact
            List<String> conversations = connexion.getConversations();
            for (String conversation : conversations) {
                afficherConversation(conversation);
            }
        } catch (IOException e) {
            // Gérer l'erreur de connexion
            System.err.println("Erreur lors de la connexion au serveur de " + contact.getNom() + " : " + e.getMessage());
        }
    }
}





    @FXML
    private void logout() throws IOException {

        App.setRoot("login");
    }
}
