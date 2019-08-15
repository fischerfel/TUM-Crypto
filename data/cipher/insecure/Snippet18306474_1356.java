import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.text.*;

import java.security.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import sun.misc.*;

import java.net.InetAddress;

/*
 * Known Bugs:
 * Message size is limited to 47 characters
 */

public class ChatClient {

    BufferedReader in;
    PrintWriter out;
    JFrame frame = new JFrame("ELECTRON Chatroom");
    JTextField textField = new JTextField(32);
    JTextArea messageArea = new JTextArea(8, 40);
    byte[] keyValue = new byte[]{'5', '7', '3', '4', '5', '6', '3', '4', '9', '8', '5', '6', 'l', '9', '3', '4'};
    final String ALGO = "AES";
    String name;
    String myName;
    InetAddress ip = null;
    String errorType;

    /*
     * Constructs the client by laying out the GUI and registering a listener
     * with the textfield so that pressing Return in the listener sends the
     * textfield contents to the server. Note however that the textfield is
     * initially NOT editable, and only becomes editable AFTER the client
     * receives the NAMEACCEPTED message from the server.
     */
public class JTextFieldLimit extends PlainDocument {
    private int limit;
    // optional uppercase conversion
    private boolean toUppercase = false;

    JTextFieldLimit(int limit) {
        super();
        this.limit = limit;
    }

    JTextFieldLimit(int limit, boolean upper) {
        super();
        this.limit = limit;
        toUppercase = upper;
    }

    public void insertString
            (int offset, String  str, AttributeSet attr)
            throws BadLocationException {
        if (str == null) return;

        if ((getLength() + str.length()) <= limit) {
            if (toUppercase) str = str.toUpperCase();
            super.insertString(offset, str, attr);
        }
    }
}
    public ChatClient() {

        // Layout GUI
        textField.setEditable(false);
        messageArea.setEditable(false);
        messageArea.setWrapStyleWord(true);
        messageArea.setLineWrap(true);
        frame.getContentPane().add(textField, "North");
        frame.getContentPane().add(new JScrollPane(messageArea), "Center");
        frame.pack();

        // Add Listeners
        textField.addActionListener(new ActionListener() {
            /**
             * Responds to pressing the enter key in the textfield by sending
             * the contents of the text field to the server. Then clear the text
             * area in preparation for the next message.
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    if ((textField.getText()).startsWith("/")) {
                        if ("Electron".equals(myName)) {
                            String command = (textField.getText());
                            out.println(command);
                            textField.setText("");
                        }
                    } else {
                        //ENCRYPTION                        
                        String encrypt = textField.getText();
                        Key key = generateKey();
                        Cipher c = Cipher.getInstance(ALGO);
                        c.init(Cipher.ENCRYPT_MODE, key);
                        byte[] encVal = c.doFinal(encrypt.getBytes());
                        String input = new BASE64Encoder().encode(encVal);
                        out.println(input);
                        textField.setText("");
                    }
                } catch (Exception ex) {
                    Logger.getLogger(ChatClient.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }

    /**
     * Prompt for and return the address of the server.
     */
    private String getServerAddress() {
        return JOptionPane.showInputDialog(
                frame,
                "Enter IP Address of the Server:",
                "ELECTRON Chatroom",
                JOptionPane.QUESTION_MESSAGE);

    }

    /**
     * Prompt for and return the desired screen name.
     */
    private String getName() {
        return JOptionPane.showInputDialog(
                frame,
                "Choose a screen name:",
                "Screen name selection",
                JOptionPane.PLAIN_MESSAGE);
    }
    /**
     * Connects to the server then enters the processing loop.
     */
    private void run() throws IOException {

        // Make connection and initialize streams
        String serverAddress = getServerAddress();
        Socket socket = new Socket(serverAddress, 9001);
        in = new BufferedReader(new InputStreamReader(
                socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);
        // Process all messages from server, according to the protocol.
        while (true) {
            String line = in.readLine();
            if (line.startsWith("SUBMITNAME")) {
                if (line.length() == 18) {
                    line = line.substring(11);
                    System.out.println(line);
                    switch (line) {
                        case "ERROR 1":
                            errorType = "[ERROR: Name cannot be blank]";
                            JOptionPane.showMessageDialog(frame, errorType);
                            System.exit(0);
                            break;
                        case "ERROR 2":
                            errorType = "[ERROR: Your names 'Admin'? Seems legit...]";
                            JOptionPane.showMessageDialog(frame, errorType);
                            System.exit(0);
                            break;
                        case "ERROR 3":
                            errorType = "[ERROR: You have been banned]";
                            JOptionPane.showMessageDialog(frame, errorType);
                            System.exit(0);
                            break;
                    }
                } else if (line.length() == 10) {
                    out.println(getName());
                }

            } else if (line.startsWith("NAMEACCEPTED")) {
                myName = line.substring(13);
                if (myName == "Admin") {
                    errorType = "[ERROR: Your names 'Admin'? Seems legit...]";
                    JOptionPane.showMessageDialog(frame, errorType);
                    System.exit(0);
                } else if (myName == "ADMIN") {
                    errorType = "[ERROR: Your names 'Admin'? Seems legit...]";
                    JOptionPane.showMessageDialog(frame, errorType);
                    System.exit(0);
                } else if (myName == "admin") {
                    errorType = "[ERROR: Your names 'Admin'? Seems legit...]";
                    JOptionPane.showMessageDialog(frame, errorType);
                    System.exit(0);
                }
                JOptionPane.showMessageDialog(frame, "Welcome " + myName + "  (" + ip + ")");
                ip = InetAddress.getLocalHost();
                textField.setEditable(true);
                //Limits message length to 47 characters (Disabled for debugging purposes)
                //textField.setDocument(new JTextFieldLimit(47));
                out.println(ip);
            } else if (line.startsWith("SERVERMESSAGE")) {
                line = line.substring(14);
                messageArea.append(line + "\n");
                messageArea.setCaretPosition(messageArea.getDocument().getLength());
            } else if (line.startsWith("SERVERCOMMAND")) {
                line = line.substring(14);
                if (line.startsWith("kick " + ip)) {
                    System.exit(0);
                }
            } else if (line.startsWith("FINGERPRINT")) {
                ip = InetAddress.getLocalHost();
                out.println(ip);
            } else if (line.startsWith("NAME")) {
                name = line.substring(5);
            } else if (line.startsWith("MESSAGE")) {
                try {
                    //DECRYPTION
                    System.out.println(line);
                    line = line.substring(8);
                    String encryptedData = line;
                    System.out.println(line.length());
                    Key key = generateKey();
                    Cipher c = Cipher.getInstance(ALGO);
                    c.init(Cipher.DECRYPT_MODE, key);
                    byte[] decordedValue = new BASE64Decoder().decodeBuffer(encryptedData);
                    byte[] decValue = c.doFinal(decordedValue);
                    String decryptedValue = new String(decValue);
                    messageArea.append(name + ": " + decryptedValue + "\n");
                    messageArea.setCaretPosition(messageArea.getDocument().getLength());
                } catch (Exception ex) {
                    Logger.getLogger(ChatClient.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    private Key generateKey() throws Exception {
        Key key = new SecretKeySpec(keyValue, ALGO);
        return key;
    }

    /**
     * Runs the client as an application with a closeable frame.
     */
    public static void main(String[] args) throws Exception {
        ChatClient client = new ChatClient();
        client.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        client.frame.setVisible(true);
        client.run();
    }
}
