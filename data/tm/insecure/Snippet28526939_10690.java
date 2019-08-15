import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.*;
import java.io.*;

import org.jivesoftware.smack.*;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.util.StringUtils;
import sun.rmi.runtime.Log;

import javax.net.ssl.*;
import javax.security.sasl.SaslException;

public class JabberSmackAPI implements MessageListener {

private XMPPConnection connection;
private final String mHost = "192.168.2.250";//if fails use "192.168.2.250" // server IP address or the
// host

public void login(String userName, String password) throws XMPPException, IOException, SmackException, NoSuchAlgorithmException, KeyManagementException {
    String service = StringUtils.parseServer(userName);
    final String user_name = StringUtils.parseName(userName);

    TrustManager[] trustAllCerts = new TrustManager[] {
            new X509TrustManager() {
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[0];
                }
                public void checkClientTrusted(
                        X509Certificate[] certs,
                        String authType) {
                }
                public void checkServerTrusted(
                        X509Certificate[] certs,
                        String authType) {
                }
            }
    };
    HostnameVerifier verifier = new HostnameVerifier() {

        @Override
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    };

    ConnectionConfiguration config = new ConnectionConfiguration(mHost, 5222);
    SSLContext context= SSLContext.getInstance("TLS");
    context.init(null, trustAllCerts, new java.security.SecureRandom());
    config.setCustomSSLContext(context);
    config.setHostnameVerifier(verifier);

    config.setSendPresence(true);
    config.setDebuggerEnabled(false);
    config.setSecurityMode(ConnectionConfiguration.SecurityMode.disabled);
    connection = new XMPPTCPConnection(config);

    SASLAuthentication.supportSASLMechanism("PLAIN", 0);
    connection.connect();
    System.out.println("JabberSmackAPI.login");
    connection.login(user_name, password);

}

public void sendMessage(String msg, String message) throws XMPPException, SmackException.NotConnectedException {
    ChatManager chatManager = ChatManager.getInstanceFor(connection);
    Chat chat = chatManager.createChat("ali", new MyMessageListener());
    chat.sendMessage(message);
}

public void displayBuddyList() {
    Roster roster = connection.getRoster();
    Collection<RosterEntry> entries = roster.getEntries();

    System.out.println("\n\n" + entries.size() + " buddy(ies):");
    for (RosterEntry r : entries) {
        System.out.println(r.getUser());
    }
}

public void disconnect() throws SmackException.NotConnectedException {
    connection.disconnect();
}

public void processMessage(Chat chat, Message message) {
    System.out.println("Received something: " + message.getBody());
    if (message.getType() == Message.Type.chat)
        System.out.println(chat.getParticipant() + " says: "
                + message.getBody());
}

public static void main(String args[]) throws XMPPException, IOException, SmackException, KeyManagementException, NoSuchAlgorithmException {
    // declare variables
    JabberSmackAPI c = new JabberSmackAPI();
    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    String msg;

    // turn on the enhanced debugger
   // XMPPConnection.DEBUG_ENABLED = true;

    // Enter your login information here
    System.out.println("-----");
    System.out.println("Login information:");

    System.out.print("username: ");
    String login_username = br.readLine();

    System.out.print("password: ");
    String login_pass = br.readLine();

    c.login(login_username, login_pass);

    c.displayBuddyList();

    System.out.println("-----");

    System.out
            .println("Who do you want to talk to? - Type contacts full email address:");
    String talkTo = br.readLine();

    System.out.println("-----");
    System.out.println("All messages will be sent to " + talkTo);
    System.out.println("Enter your message in the console:");
    System.out.println("-----\n");

    while (!(msg = br.readLine()).equals("bye")) {
        c.sendMessage(msg, talkTo);
    }

    c.disconnect();
    System.exit(0);
}

private class MyMessageListener implements MessageListener {
    @Override
    public void processMessage(Chat chat, Message message) {
        String from = message.getFrom();
        String body = message.getBody();
        System.out.println(String.format("Received message " + body + " from " + from));
    }
}

}
