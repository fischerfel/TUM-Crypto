import java.io.*;
import java.net.*;
import java.security.*;
import javax.crypto.*;

public class PKServer
{
    public void decryptMessage(InputStream inStream) throws IOException, NoSuchAlgorithmException
    {
        try {

            //Create the Data input stream from the socket
            DataInputStream dis = new DataInputStream(inStream);

            //Get the key
            ObjectInputStream in = new ObjectInputStream(new FileInputStream("KeyFile.xx"));

            //ObjectOutputStream outSocket = new ObjectOutputStream(s.getOutputStream());

            PrivateKey privatekey = (PrivateKey) in.readObject();
            System.out.println("Key Used: " + in.toString());
            in.close();

            //Initiate the cipher
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");                        
            cipher.init(Cipher.DECRYPT_MODE,privatekey);

            int len = dis.readInt();
            byte[] encryptedMsg = new byte[len];
            dis.readFully(encryptedMsg);         

            System.out.println("Server - Msg Length: " + len);
            System.out.println("Server - Encrypted: " + asHex(encryptedMsg));

            // -Print out the decrypt String to see if it matches the original message.
            byte[] plainText = cipher.doFinal(encryptedMsg);
            System.out.println("Decrypted Message: " + new String(plainText, "SHA"));


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
    public static void main(String[] args) throws Exception 
    {
        int port = 7999;
        ServerSocket server = new ServerSocket(port);
        Socket s = server.accept();                     


        PKServer cs = new PKServer();
        cs.decryptMessage(s.getInputStream());

        server.close();
    }
}
