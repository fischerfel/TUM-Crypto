package encryption;

import java.io.*;
import java.net.*;
import java.security.*;
import javax.crypto.*;

public class CipherClient
{
    public static void main(String[] args) throws Exception 
    {
        // -Generate a DES key.
        KeyGenerator generator = KeyGenerator.getInstance("DES");
        generator.init(new SecureRandom());
        Key key = generator.generateKey();

        // -Store it in a file.
        ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("KeyFile.xx"));
        out.writeObject(key);
        out.close();

        // -Connect to a server.
        String message = "The quick brown fox jumps over the lazy dog.";
        String host = "localhost";
        int port = 7999;
        Socket s = new Socket(host, port);

        // -Use the key to encrypt the message above and send it over socket s to the server.   
        Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] encVal = cipher.doFinal(message.getBytes());
        DataOutputStream dOut = new DataOutputStream(s.getOutputStream());
        dOut.writeInt(encVal.length); // write length of the message
        dOut.write(encVal);           // write the message
    }
}
