import java.io.*;
import java.util.*;
import java.net.*;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
//import javax.crypto.CipherInputStream;
//import javax.crypto.CipherOutputStream;

import java.security.spec.AlgorithmParameterSpec;
import javax.crypto.spec.SecretKeySpec;

class clientHandler {
    /*
     *  This class sets up AES CBC encryption for the socket, 
     *  new instance for every connection since the IV changes
     */

    // Declare variables
    private Cipher ecipher;
    private Cipher dcipher;
    Socket testSocket = null;
    //DataOutputStream out = null;
    //DataInputStream in = null;
    PrintWriter out = null;
    BufferedReader in = null;
    String masterkey = "mysecretpassword";
    static final String HEXES = "0123456789ABCDEF";

    public static String byteToHex( byte [] raw ) {
        if ( raw == null ) {
            return null;
        }
        final StringBuilder hex = new StringBuilder( 2 * raw.length );
        for ( final byte b : raw ) {
            hex.append(HEXES.charAt((b & 0xF0) >> 4))
                .append(HEXES.charAt((b & 0x0F)));
        }
        return hex.toString();
    }

    public static byte[] hexToByte(String hexString) {
        int len = hexString.length();
        byte[] ba = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            ba[i/2] = (byte) ((Character.digit(hexString.charAt(i), 16) << 4) + Character.digit(hexString.charAt(i+1), 16));
        }
        return ba;
    }

    public String encrypt(String plaintext) {
        // encrypt string
        try {
            byte[] cipherbyte = ecipher.doFinal(plaintext.getBytes("UTF-8")); 
            //String ciphertext = new String(ecipher.doFinal(plaintext.getBytes("UTF8")));
            return byteToHex(cipherbyte);
        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public String decrypt(String ciphertext) {
        // decrypt hex string
        try {
            System.out.println("decrypt byte length: " + hexToByte(ciphertext).length);
            String tp = new String(hexToByte(ciphertext), "UTF-8");
            System.out.println("toString(): " + tp);
            String plaintext = new String(dcipher.doFinal(hexToByte(ciphertext.trim())), "UTF-8");
            //String plaintext = new String(dcipher.doFinal(ciphertext.getBytes("UTF8")), "UTF-8");
            return plaintext;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void setupCrypto(byte[] iv, String key) {
        // setup AES CBC encryption
        //  convert IV and key to byte array for crypto
        try {
             System.out.println("Setting up Crypto...");
             //byte[] ivb = iv.getBytes("UTF8");
             byte[] keyb = key.getBytes("UTF8");
             AlgorithmParameterSpec paramSpec = new IvParameterSpec(iv);
             SecretKeySpec skey = new SecretKeySpec(keyb, "AES");
             ecipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
             dcipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
             ecipher.init(Cipher.ENCRYPT_MODE, skey, paramSpec);
             dcipher.init(Cipher.DECRYPT_MODE, skey, paramSpec);
        } catch (Exception e) {
            System.err.println("Error:" + e);
        }
    }

    public void startServer() {
        // starts server
        try {
            System.out.println("Connecting to Server");
            testSocket = new Socket("localhost", 5555);
            //out = new DataOutputStream(testSocket.getOutputStream());
            //in = new DataInputStream(testSocket.getInputStream());
            out = new PrintWriter(testSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(testSocket.getInputStream()));
        } catch (UnknownHostException e) {
            System.err.println("Uknown Host");
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection");
        }
    }

     public void androidClientHandler() {
         // actual communication
         if (testSocket != null && out != null && in != null) {
             try {
                 System.out.println("Waiting for IV..");
                 String iv;
                 iv = in.readLine();
                 byte [] ivb = hexToByte(iv);
                 System.out.println("Got IV..." + iv);
                 setupCrypto(ivb, masterkey);
                 String ciphertext;
                 String plaintext;
                 System.out.println("Sending \"test\" to server");
                 //out.writeBytes("test\n");
                 ciphertext = encrypt("test");
                 System.out.println("Sent: " + ciphertext);
                 out.println(ciphertext);
                 System.out.println("Waiting for Server to reply");
                 String responseLine;
                 responseLine = in.readLine().replaceAll("\\\\n", "");
                 System.out.println("Recieved from Server: " + responseLine + " - length: " + responseLine.length());
                 plaintext = decrypt(responseLine);
                 System.out.println("Recieved from Server: " + plaintext);
                 System.out.println("Closing Connection");
                 out.close();
                 in.close();
                 testSocket.close();
             } catch (UnknownHostException e) {
                 System.err.println("Trying to connect to unknown host:" + e);
             } catch (IOException e) {
                 System.err.println("IOException: " + e);
             }
         }
     }

     public void javaIsGay() {
         startServer();
         androidClientHandler();
     }

     public static void main(String[] args) {
         System.setProperty("file.encoding", "UTF-8");
         clientHandler c = new clientHandler();
         c.javaIsGay();
     }
}
