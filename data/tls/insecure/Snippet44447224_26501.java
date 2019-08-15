import java.io.*;
import java.net.*;
import java.util.*;
import javax.net.ssl.*;
import java.security.*;

public class ChatServer {

protected List<Socket> clients = new ArrayList<Socket>(); // list of clients

private String[][] portUserPair = new String[10][2];
int i = 0;

private String passphrase = "serverpwd";
private int port = 1234;

public static void main(String[] args) throws Exception {
    new ChatServer();
}

public ChatServer() throws Exception {

    KeyStore clientKeyStore = KeyStore.getInstance("JKS");
    clientKeyStore.load(new FileInputStream("client.public"), "public".toCharArray());

    KeyStore serverKeyStore = KeyStore.getInstance("JKS");
    serverKeyStore.load(new FileInputStream("server.private"), passphrase.toCharArray());

    TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");
    tmf.init(clientKeyStore);
    KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
    kmf.init(serverKeyStore, passphrase.toCharArray());
    SSLContext sslContext = SSLContext.getInstance("TLS");
    SSLContext.setDefault(sslContext);
    sslContext.init(kmf.getKeyManagers(), tmf.getTrustManagers(), (new SecureRandom()));

    SSLServerSocketFactory factory = (SSLServerSocketFactory) SSLServerSocketFactory.getDefault();
    SSLServerSocket ss = (SSLServerSocket) factory.createServerSocket(port);
    ss.setNeedClientAuth(false);
    ss.setEnabledCipherSuites(new String[] {"TLS_RSA_WITH_AES_128_CBC_SHA"});


    // start listening for new connections
    System.out.println("[system] listening ...");
    try {
        while (true) {
            Socket newClientSocket = ss.accept(); // wait for a new client connection
            ((SSLSocket)newClientSocket).startHandshake();
            String username = ((SSLSocket) newClientSocket).getSession().getPeerPrincipal().getName();
            System.out.println("Established SSL connection with: " + username);
            synchronized(this) {
                portUserPair[i][0] = Integer.toString(newClientSocket.getPort());
                clients.add(newClientSocket); // add client to the list of clients
            }
            ChatServerConnector conn = new ChatServerConnector(this, newClientSocket); // create a new thread for communication with the new client
            conn.start(); // run the new thread
        }
    } catch (Exception e) {
        System.err.println("[error] Accept failed.");
        e.printStackTrace(System.err);
        System.exit(1);
    }

    // close socket
    System.out.println("[system] closing server socket ...");
    try {
        ss.close();
    } catch (IOException e) {
        e.printStackTrace(System.err);
        System.exit(1);
    }
}

// send a message to all clients connected to the server
public void sendToAllClients(String message) throws Exception {
    Iterator<Socket> i = clients.iterator();
    while (i.hasNext()) { // iterate through the client list
        Socket socket = (Socket) i.next(); // get the socket for communicating with this client
        try {
            DataOutputStream out = new DataOutputStream(socket.getOutputStream()); // create output stream for sending messages to the client
            out.writeUTF(message); // send message to the client
        } catch (Exception e) {
            System.err.println("[system] could not send message to a client");
            e.printStackTrace(System.err);
        }
    }
}

public void sendToAClient(String message, String userInfo) throws Exception {
    Socket userSocket = null;
    String userPort = "";
    Iterator<Socket> i = clients.iterator();
    for (int k = 0; k < portUserPair.length; k++) {
        if(portUserPair[k][1].equals(userInfo)) {
            userPort = portUserPair[k][0];
            break;
        }
    }

    System.out.println(userPort);

    while (i.hasNext()) { // iterate through the client list
        Socket socket = (Socket) i.next(); // get the socket for communicating with this client
        if(Integer.toString(socket.getPort()).equals(userPort)) {
            userSocket = socket;
            break;
        }
    }

    message = message.substring(userInfo.length()+2);

    try {
        DataOutputStream out = new DataOutputStream(userSocket.getOutputStream()); // create output stream for sending messages to the client
        out.writeUTF(message); // send message to the client
    } catch (Exception e) {
        System.err.println("[system] could not send message to the client");
        e.printStackTrace(System.err);
    }
}

public void removeClient(Socket socket) {
    synchronized(this) {
        clients.remove(socket);
    }
}

public void saveUserInfo(String userInfo) {
    portUserPair[i][1] = userInfo;
    System.out.println(portUserPair[i][0] + " : " + portUserPair[i][1]);
    i++;
}
}

class ChatServerConnector extends Thread {
private ChatServer server;
private Socket socket;

public ChatServerConnector(ChatServer server, Socket socket) {
    this.server = server;
    this.socket = socket;
}

public void run() {
    System.out.println("[system] connected with " + this.socket.getInetAddress().getHostName() + ":" + this.socket.getPort());

    DataInputStream in;
    try {
        in = new DataInputStream(this.socket.getInputStream()); // create input stream for listening for incoming messages
    } catch (IOException e) {
        System.err.println("[system] could not open input stream!");
        e.printStackTrace(System.err);
        this.server.removeClient(socket);
        return;
    }

    while (true) { // infinite loop in which this thread waits for incoming messages and processes them
        String msg_received;
        try {
            msg_received = in.readUTF(); // read the message from the client
        } catch (Exception e) {
            System.err.println("[system] there was a problem while reading message client on port " + this.socket.getPort());
            e.printStackTrace(System.err);
            this.server.removeClient(this.socket);
            return;
        }

        if (msg_received.length() == 0) // invalid message
            continue;

        String[] msg = msg_received.split(" ");
        System.out.println(msg_received);

        if (msg[1].equals("system")) {
//            try {
//                server.saveUserInfo(((SSLSocket) socket).getSession().getPeerPrincipal().getName());
//            } catch (SSLPeerUnverifiedException ex) {
//                Logger.getLogger(ChatServerConnector.class.getName()).log(Level.SEVERE, null, ex);
//            }
            continue;
        }

        System.out.println(msg_received); // print the incoming message in the console

        String msg_send = msg_received.toUpperCase();

        if (msg[3].charAt(0) == '/') {
            String[] privateMsg = msg[3].split(" ");
            try {
                this.server.sendToAClient(msg_send, privateMsg[0].replace("/", "")); // send message to the client
            } catch (Exception e) {
                System.err.println("[system] there was a problem while sending the message to the client");
                e.printStackTrace(System.err);
                continue;
            }
            continue;
        }

        try {
            this.server.sendToAllClients(msg_send); // send message to all clients
        } catch (Exception e) {
            System.err.println("[system] there was a problem while sending the message to all clients");
            e.printStackTrace(System.err);
            continue;
        }
    }
}
}
