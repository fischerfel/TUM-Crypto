import java.io.*;
import java.net.*;
import java.security.NoSuchAlgorithmException;

import javax.crypto.*;


public class CipherServer
{
public static void main(String[] args) throws Exception 
{
    //Start socket server
    int port = 8001;
    ServerSocket s = new ServerSocket();
    s.setReuseAddress(true);
    s.bind(new InetSocketAddress(port));
    Socket client = s.accept();

    CipherServer server = new CipherServer();
    server.decryptMessage(client.getInputStream());

    s.close();
}


public void decryptMessage(InputStream inStream) throws IOException, NoSuchAlgorithmException
{
    try {

        //Create the Data input stream from the socket
        DataInputStream in = new DataInputStream(inStream);

        //Get the key
        ObjectInputStream file = new ObjectInputStream(new FileInputStream("KeyFile.xx"));
        SecretKey key = (SecretKey) file.readObject();
        System.out.println("Key Used: " + file.toString());
        file.close();

        //Initiate the cipher
        //Cipher d = Cipher.getInstance("DES/ECB/PKCS5Padding");                        
        Cipher d = Cipher.getInstance("DES/ECB/NoPadding");
        d.init(Cipher.DECRYPT_MODE,key);

        int len = in.readInt();
        byte[] encryptedMsg = new byte[len];
        in.readFully(encryptedMsg);         

        System.out.println("Server - Msg Length: " + len);
        System.out.println("Server - Encrypted: " + asHex(encryptedMsg));


        //String demsg = new String(d.doFinal(encryptedMsg), "UTF-8");
        //System.out.println("Decrypted Message: " + demsg);
        // -Print out the decrypt String to see if it matches the orignal message.
        byte[] plainText = d.doFinal(encryptedMsg);
        System.out.println("Decrypted Message: " + new String(plainText, "UTF-8"));


    } catch (Exception e) {
        e.printStackTrace();
    }
}

//Function to make the bytes printable (hex format)
public static String asHex(byte buf[]) {
    StringBuilder strbuf = new StringBuilder(buf.length * 2);
    int i;
    for (i = 0; i < buf.length; i++) {
        if (((int) buf[i] & 0xff) < 0x10) {
            strbuf.append("0");
        }
        strbuf.append(Long.toString((int) buf[i] & 0xff, 16));
    }
    return strbuf.toString();
}
 }
