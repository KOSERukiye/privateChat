package univ_lorraine.iut.java.privatechat.ClientServeur;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigInteger;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.*;
import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;

public class Serveur {
    private static ServerSocket server;
    private static int port = 9876;
    private static Boolean running = true;
    private static PublicKey clientPublicKey;
    private static PrivateKey serverPrivateKey;
    private static byte[] sharedSecretKey;

    public static void main(String args[]) throws IOException, ClassNotFoundException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        server = new ServerSocket(port);
        while (running) {
            System.out.println("Waiting for the client request");
            Socket socket = server.accept();

            // Générer une paire de clés pour le serveur
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("DH");
            keyGen.initialize(2048);
            KeyPair serverKeyPair = keyGen.generateKeyPair();

            // Recevoir la clé publique du client
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            clientPublicKey = (PublicKey) ois.readObject();

            // Envoyer la clé publique du serveur au client
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            PublicKey serverPublicKey = serverKeyPair.getPublic();
            oos.writeObject(serverPublicKey);

            // Créer l'objet KeyAgreement pour le serveur et initialiser avec la clé privée
            KeyAgreement serverKA = KeyAgreement.getInstance("DH");
            serverPrivateKey = serverKeyPair.getPrivate();
            serverKA.init(serverPrivateKey);

            // Générer la clé partagée en utilisant la clé publique du client
            serverKA.doPhase(clientPublicKey, true);
            byte[] serverSharedSecret = serverKA.generateSecret();

            // Recevoir la clé secrète chiffrée du client et la déchiffrer
            Cipher cipher = Cipher.getInstance("AES");
            byte[] encryptedSharedSecret = (byte[]) ois.readObject();
            cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(serverSharedSecret, "AES"));
            byte[] decryptedSharedSecret = cipher.doFinal(encryptedSharedSecret);

            sharedSecretKey = decryptedSharedSecret;

            Thread thread = new Thread(new ClientCommunication(socket, sharedSecretKey));
            thread.start();
        }
        System.out.println("Shutting down Socket server!!");
        server.close();
    }

}