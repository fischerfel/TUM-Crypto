import java.awt.*;
import java.awt.event.*;    

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.swing.*;       
import javax.swing.event.*;
import java.io.*;
import java.net.*;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPublicKeySpec;

public class chatClient extends SwingChatGUI
{

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    static Socket socket = null;
    static PrintWriter out = null;
    static BufferedReader in = null;
    public ButtonHandler bHandler;
    static PublicKey publicKey;
    static PrivateKey privateKey;
    String fromServer;
    int stage = 0;

    public chatClient (String title)
    {
        super (title);
        bHandler = new ButtonHandler();
        sendButton.addActionListener( bHandler );
    }

    private class ButtonHandler implements ActionListener
    {
        public void actionPerformed (ActionEvent event)
        {
            String outputLine;
            outputLine = txArea.getText ();
            System.out.println ("Client > " + outputLine);
            out.println (outputLine);
        }
    }

    public void decrypt() {


         Cipher cipher1;
        try {

            //fromServer = fromServer.substring(0, fromServer.length() - 2);
            int count = fromServer.length();
            System.out.println("the length is " +count);
            byte[] src = fromServer.getBytes();
            cipher1 = Cipher.getInstance("RSA");
              cipher1.init(Cipher.DECRYPT_MODE, privateKey);
              byte[] cipherData1 = cipher1.doFinal(src);
              System.out.println("encrypted?" + new String(cipherData1));



        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (BadPaddingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


    }

    public void run () throws IOException
    {
        try
        {
            socket = new Socket ("localhost", 4444);
            out = new PrintWriter (socket.getOutputStream (), true);
            in = new BufferedReader (new InputStreamReader (socket.getInputStream ()));
        }
        catch (UnknownHostException e)
        {
            System.err.println ("Don't know about host: to host.");
            System.exit(1);
        }
        catch (IOException e)
        {
            System.err.println ("Couldn't get I/O for the connection to: to host.");
            System.exit (1);
        }


        kp();
        CharArrayWriter os = new CharArrayWriter();
        char[] buf = new char[4096];
        int read;
        while ((read=in.read(buf, 0, 4096)) > 0) {
          System.out.println("x");
                 os.write(buf, 0, read);
          fromServer = os.toString();

            if (stage > 2)
            {
                decrypt();
            }
            stage++;
            System.out.println ("Client <  " + fromServer);
            rxArea.setText (fromServer);
            os.reset();
            if (fromServer.equals ("Bye.")) break;
        }

        out.close();
        in.close();
        socket.close();
    }

    public static void main(String[] args) 
    {

        chatClient f = new chatClient ("Chat Client Program");

        f.pack ();
        f.show ();
        try
        {
            f.run ();
        }
        catch (IOException e)
        {
            System.err.println("Couldn't get I/O for the connection to: to host.");
            System.exit(1);
        }
    }


    public static void kp() {

        KeyPairGenerator kpg;

            try {
                kpg = KeyPairGenerator.getInstance("RSA");
                kpg.initialize(2048);
                KeyPair kp = kpg.genKeyPair();
                publicKey = kp.getPublic();
                privateKey = kp.getPrivate();
                KeyFactory fact = KeyFactory.getInstance("RSA");
                RSAPublicKeySpec pub;

                    pub = fact.getKeySpec(kp.getPublic(),
                          RSAPublicKeySpec.class);
                    out.println("!m! " + pub.getModulus() + "!e! " + pub.getPublicExponent());
                } catch (InvalidKeySpecException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();






            } catch (NoSuchAlgorithmException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }




    }



}
