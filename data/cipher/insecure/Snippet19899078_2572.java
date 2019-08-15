import java.io.*;
import java.net.*;
import java.security.*;

import javax.crypto.*;

public class CipherClient
{public static void main(String[] args) throws Exception 
{
    try {
        //Starts socket
        String host = "localhost";
        int port = 8001;
        Socket s = new Socket(host, port);

        //Generate a DES key.
        KeyGenerator keygen = KeyGenerator.getInstance("DES");
        keygen.init(56, new SecureRandom());
        SecretKey key = keygen.generateKey();           

        //Store the key in a file
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("KeyFile.xx"));
        oos.writeObject(key);
        oos.close();

        //Start Cipher Instance and cipher the message
        //Cipher c = Cipher.getInstance("DES/ECB/PKCS5Padding");            
        Cipher c = Cipher.getInstance("DES/ECB/NoPadding");
        c.init(Cipher.ENCRYPT_MODE,key);

        //Get string and encrypted version
        //String message = "The quick brown fox jumps over the lazy dog.";
        String message = "12345678";
        byte[] encryptedMsg = c.doFinal(message.getBytes("UTF-8"));

        System.out.println("Client - Message: " + message);
        System.out.println("Client - Encrypted: " + CipherServer.asHex(encryptedMsg));


        //TEST DECRYPT W/ KEY FILE W/O SERVER (WORKS!)
        //-------------------------------

        //Read key from file test
            ObjectInputStream file = new ObjectInputStream(new FileInputStream("KeyFile.xx"));
            SecretKey key2 = (SecretKey) file.readObject();
            System.out.println("Key Used: " + file.toString());
            file.close();

        //Decrypt Test
            c.init(Cipher.DECRYPT_MODE,key2);
            byte[] plainText = c.doFinal(encryptedMsg);
            System.out.println("Decrypted Message: " + new String(plainText));


        //Open stream to cipher server
        DataOutputStream os = new DataOutputStream(s.getOutputStream());
        os.writeInt(encryptedMsg.length);
        os.write(encryptedMsg);

        os.flush();
        os.close();

        //Close socket
        s.close();

    }catch (Exception e) {
        e.printStackTrace();
    }
}
 }
