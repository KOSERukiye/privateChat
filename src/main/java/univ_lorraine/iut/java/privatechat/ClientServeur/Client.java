package univ_lorraine.iut.java.privatechat.ClientServeur;

import java.io.*;
import java.net.*;
import javax.crypto.*;
import javax.crypto.spec.*;
import java.security.*;
import java.math.*;

public class Client {
    public static void main(String[] args) {
        try {
            // 1. Création d'un socket client et initialisation des flux
            InetAddress host = InetAddress.getLocalHost();
            Socket socket = new Socket(host.getHostName(), 9876);
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

            // 2. Génération de la paire de clés Diffie-Hellman
            KeyPairGenerator kpg = KeyPairGenerator.getInstance("DH");
            kpg.initialize(1024);
            KeyPair kp = kpg.generateKeyPair();
            Key publicKey = kp.getPublic();
            Key privateKey = kp.getPrivate();

            // 3. Envoi de la clé publique au serveur
            out.writeObject(publicKey);
            out.flush();

            // 4. Réception de la clé publique du serveur
            Key serverPublicKey = (Key)in.readObject();

            // 5. Génération de la clé secrète partagée
            KeyAgreement ka = KeyAgreement.getInstance("DH");
            ka.init(privateKey);
            ka.doPhase(serverPublicKey, true);
            byte[] secret = ka.generateSecret();

            // 6. Création d'un objet Cipher pour chiffrer les messages avec la clé secrète partagée
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            SecretKeySpec secretKeySpec = new SecretKeySpec(secret, "AES");
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);

            // 7. Envoi des messages chiffrés au serveur
            String message = "Bonjour";
            byte[] encrypted = cipher.doFinal(message.getBytes());
            out.writeObject(encrypted);
            out.flush();

            // 8. Fermeture des flux et du socket
            out.close();
            in.close();
            socket.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
