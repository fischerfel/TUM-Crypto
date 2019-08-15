    import java.awt.*;
import java.math.BigInteger;
import java.net.*;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPublicKeySpec;
import java.awt.*;          
import java.awt.event.*;    

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.swing.*;       
import javax.swing.event.*;
import java.io.*;

public class chatServer extends SwingChatGUI
{
    PrintWriter out;
    BufferedReader in;
    BufferedReader stdin;
    String inputLine, outputLine;
    public ButtonHandler bHandler = new ButtonHandler();
    int stage = 1;
    private static BigInteger l;
    private static BigInteger m;
    PublicKey pubKey;

    public chatServer (String title)
    {
        super (title);
        bHandler = new ButtonHandler ();
        sendButton.addActionListener (bHandler);
    }

    private class ButtonHandler implements ActionListener
    {
        public void actionPerformed (ActionEvent event)
        {
            outputLine = txArea.getText ();
            System.out.println ("Server > " + outputLine);
            encrypt();
            out.println (outputLine);
        }
    }

    public void run () throws IOException
    {
        ServerSocket serverSocket = null;

        try
        {
            serverSocket = new ServerSocket (4444);
        }
        catch (IOException e)
        {
            System.err.println ("Could not listen on port: 4444.");
            System.exit (1);
        }

        Socket clientSocket = null;
        try
        {
            clientSocket = serverSocket.accept ();
        }
        catch (IOException e)
        {
            System.err.println ("Accept failed.");
            System.exit(1);
        }

        out = new PrintWriter (clientSocket.getOutputStream (), true);
        in = new BufferedReader (new InputStreamReader (clientSocket.getInputStream ()));
        //stdin = new BufferedReader (new InputStreamReader (System.in));

        out.println ("Welcome to the Chat Server\n");

        while ((inputLine = in.readLine ()) != null)
        {
            System.out.println ("Server < " + inputLine);
            rxArea.setText (inputLine);

            if (stage == 1)
            {
                String[] parts = inputLine.split("!e! ", 2);
                String string1 = parts[0];
                String string2 = parts[1];


                String j = string1.replace("!m! ", "");


                m = new BigInteger(j);
                l = new BigInteger(string2);

                RSAPublicKeySpec keySpec = new RSAPublicKeySpec(m, l);
                KeyFactory fact;
                try {
                    fact = KeyFactory.getInstance("RSA");
                     pubKey = fact.generatePublic(keySpec);
                } catch (NoSuchAlgorithmException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (InvalidKeySpecException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
               stage++;
            }


        }

        out.close();
        in.close();
        clientSocket.close();
        serverSocket.close();
    }

    public void encrypt() {

        byte[] src = outputLine.getBytes();
         Cipher cipher;
        try {
            cipher = Cipher.getInstance("RSA");
              cipher.init(Cipher.ENCRYPT_MODE, pubKey);
              byte[] cipherData = cipher.doFinal(src);
              outputLine = new String(cipherData);
              int count = outputLine.length();
              System.out.println("bytes: " + count);
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

    public static void main(String[] args) //throws IOException
    {

        chatServer f = new chatServer ("Chat Server Program");

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
}
