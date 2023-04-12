import java.io.*;
import java.net.*;
import javax.crypto.*;
import javax.crypto.spec.*;
import java.security.*;
import java.math.*;

public class Serveur {
    //static ServerSocket variable
    private static ServerSocket server;
    //socket server port on which it will listen
    private static int port = 9876;
    private static Boolean running=true;

    public static void main(String args[]) throws IOException, ClassNotFoundException{
        //create the socket server object
        server = new ServerSocket(port);
        //keep listens indefinitely until receives 'exit' call or program terminates
        List<Thread> threadList = new ArrayList<>();
        while(running){
            System.out.println("Waiting for the client request");
            Socket socket = server.accept();
            Thread thread = new Thread(new ClientCommunication(socket));
            threadList.add(thread);
            thread.start();
        }
        System.out.println("Shutting down Socket server!!");
        //close the ServerSocket object
        for(Thread thread:threadList) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        server.close();
    }

    private static class ClientCommunication implements Runnable {
        private Socket socket;

        public ClientCommunication(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try {
                // 2. Initialisation des flux
                ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
                ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

                // 3. Génération de la paire de clés Diffie-Hellman
                KeyPairGenerator kpg = KeyPairGenerator.getInstance("DH");
                kpg.initialize(1024);
                KeyPair kp = kpg.generateKeyPair();
                Key publicKey = kp.getPublic();
                Key privateKey = kp.getPrivate();

                // 4. Envoi de la clé publique du serveur au client
                out.writeObject(publicKey);
                out.flush();

                // 5. Réception de la clé publique du client
                Key clientPublicKey = (Key)in.readObject();

                // 6. Génération de la clé secrète partagée
                KeyAgreement ka = KeyAgreement.getInstance("DH");
                ka.init(privateKey);
                ka.doPhase(clientPublicKey, true);
                byte[] secret = ka.generateSecret();

                // 7. Création d'un objet Cipher pour déchiffrer les messages avec la clé secrète partagée
                Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
                SecretKeySpec secretKeySpec = new SecretKeySpec(secret, "AES");
                cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);

                // 8. Réception des messages chiffrés et déchiffrement avec la clé secrète partagée
                while (true) {
                    String message = (String) in.readObject();
                    if (message.equals("exit")) {
                        break;
                    }
                    byte[] messageBytes = Base64.getDecoder().decode(message);
                    byte[] decryptedBytes = cipher.doFinal(messageBytes);
                    String decryptedMessage = new String(decryptedBytes);
                    System.out.println("Message reçu: " + decryptedMessage);

                    // 9. Chiffrement de la réponse avec la clé secrète partagée et envoi au client
                    String responseMessage = "Serveur: J'ai bien reçu votre message!";
                    byte[] responseBytes
                }