package Chat.Application;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import java.security.spec.AlgorithmParameterSpec;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import sun.misc.BASE64Encoder;

/**
 * A simple Swing-based client for the chat server. Graphically it is a frame
 * with a text field for entering messages and a textarea to see the whole
 * dialog.
 *
 * The client follows the Chat Protocol which is as follows. When the server
 * sends "SUBMITNAME" the client replies with the desired screen name. The
 * server will keep sending "SUBMITNAME" requests as long as the client submits
 * screen names that are already in use. When the server sends a line beginning
 * with "NAMEACCEPTED" the client is now allowed to start sending the server
 * arbitrary strings to be broadcast to all chatters connected to the server.
 * When the server sends a line beginning with "MESSAGE " then all characters
 * following this string should be displayed in its message area.
 */
public class ChatClient {

    BufferedReader in;
    PrintWriter out;
    JFrame frame = new JFrame("ELECTRON Chatroom");
    JTextField textField = new JTextField(40);
    JTextArea messageArea = new JTextArea(8, 40);
        Cipher cipher;
        // password for encryption
        final String strPassword = "1234567890123456";
        // put this as key in AES
        final SecretKeySpec key = new SecretKeySpec(strPassword.getBytes(), "AES");
    /**
     * Constructs the client by laying out the GUI and registering a listener
     * with the textfield so that pressing Return in the listener sends the
     * textfield contents to the server. Note however that the textfield is
     * initially NOT editable, and only becomes editable AFTER the client
     * receives the NAMEACCEPTED message from the server.
     */
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
                    String input = (textField.getText());
                    //ENCRYPTION
                    // Parameter specific algorithm
                    AlgorithmParameterSpec paramSpec = new IvParameterSpec(strPassword.getBytes());
                    //Whatever you want to encrypt/decrypt
                    Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");

                    // You can use ENCRYPT_MODE (ENCRYPTunderscoreMODE)  or DECRYPT_MODE (DECRYPT underscore MODE) 

                    cipher.init(Cipher.ENCRYPT_MODE, key, paramSpec);

                    // encrypt data 
                    byte[] encrypted = cipher.doFinal(input.getBytes());

                    // encode data using standard encoder
                    //String output = new BASE64Encoder().encode(encrypted);

                    System.out.println("Orginal tring: " + input);
                    System.out.println("Encrypted string: " + encrypted);

                    textField.setText("");
                } catch (NoSuchAlgorithmException | NoSuchPaddingException | IllegalBlockSizeException | BadPaddingException | InvalidKeyException | InvalidAlgorithmParameterException ex) {
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
    private void run() throws IOException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException, InvalidAlgorithmParameterException {

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
                out.println(getName());
            } else if (line.startsWith("NAMEACCEPTED")) {
                textField.setEditable(true);
            } else if (line.startsWith("MESSAGE")) {
                //DECRYPTION
                line = line.substring(8);
                System.out.println(line);
                // Parameter specific algorithm
                AlgorithmParameterSpec paramSpec = new IvParameterSpec(strPassword.getBytes());
                //Whatever you want to encrypt/decrypt
                Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");

                // You can use ENCRYPT_MODE (ENCRYPTunderscoreMODE)  or DECRYPT_MODE (DECRYPT underscore MODE) 
                cipher.init(Cipher.DECRYPT_MODE, key, paramSpec);
                byte messageByte[] = cipher.doFinal(line.getBytes());
                String message = new String(messageByte, "UTF-8");
                messageArea.append(message.substring(8) + "\n");
                System.out.println(message);
            }
        }
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
