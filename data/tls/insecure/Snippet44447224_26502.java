import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;
import javax.net.ssl.*;
import java.security.*;

public class ChatClient extends Thread {

    private String userInfo = "";

    private String passphrase = "pwd";
    private int port = 1234;

    public static void main(String[] args) throws Exception {
        new ChatClient();
    }

    public ChatClient() throws Exception {

        KeyStore serverKeyStore = KeyStore.getInstance("JKS");
        serverKeyStore.load(new FileInputStream("server.public"), "public".toCharArray());

        KeyStore clientKeyStore = KeyStore.getInstance("JKS");
        Scanner s = new Scanner(System.in);
        String username = s.nextLine();
        clientKeyStore.load(new FileInputStream(username + ".private"), (username + passphrase).toCharArray());

        TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");
        tmf.init(serverKeyStore);
        KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
        kmf.init(clientKeyStore, (username + passphrase).toCharArray());
        SSLContext sslContext = SSLContext.getInstance("TLS");
        SSLContext.setDefault(sslContext);
        sslContext.init(kmf.getKeyManagers(), tmf.getTrustManagers(), new SecureRandom());

        SSLSocketFactory sf = sslContext.getSocketFactory();
        SSLSocket socket = (SSLSocket) sf.createSocket("localhost", port);
        socket.setEnabledCipherSuites(new String[] {"TLS_RSA_WITH_AES_128_CBC_SHA"});
        socket.startHandshake();

        DataInputStream in = null;
        DataOutputStream out = null;

        // connect to the chat server
        try {
            System.out.println("[system] connecting to chat server ...");
            System.out.println("[system] you are signed in as: " + username);

            in = new DataInputStream(socket.getInputStream()); // create input stream for listening for incoming messages
            out = new DataOutputStream(socket.getOutputStream()); // create output stream for sending messages

            this.sendMessage(username, out, "system");

            System.out.println("[system] connected");
            System.out.println("[system] to send a private message type \"/<NameOfUser>\" and then type the message");

            ChatClientMessageReceiver message_receiver = new ChatClientMessageReceiver(in); // create a separate thread for listening to messages from the chat server
            message_receiver.start(); // run the new thread
        } catch (Exception e) {
            e.printStackTrace(System.err);
            System.exit(1);
        }

        // read from STDIN and send messages to the chat server
        BufferedReader std_in = new BufferedReader(new InputStreamReader(System.in));
        String userInput;
        while ((userInput = std_in.readLine()) != null) { // read a line from the console
            this.sendMessage(userInput, out, userInfo); // send the message to the chat server
        }

        // cleanup
        out.close();
        in.close();
        std_in.close();
        socket.close();
    }

    private void sendMessage(String message, DataOutputStream out, String userInfo) {
        message = "[" + new SimpleDateFormat("HH:mm:ss").format(new Date()) + "] " + userInfo + " : " + message;
        try {
            out.writeUTF(message); // send the message to the chat server
            out.flush(); // ensure the message has been sent
        } catch (IOException e) {
            System.err.println("[system] could not send message");
            e.printStackTrace(System.err);
        }
    }
}

// wait for messages from the chat server and print the out
class ChatClientMessageReceiver extends Thread {
    private DataInputStream in;

    public ChatClientMessageReceiver(DataInputStream in) {
        this.in = in;
    }

    public void run() {
        try {
            String message;
            while ((message = this.in.readUTF()) != null) { // read new message
                System.out.println(message); // print the message to the console
            }
        } catch (Exception e) {
            System.err.println("[system] could not read message");
            e.printStackTrace(System.err);
        }
    }
}
