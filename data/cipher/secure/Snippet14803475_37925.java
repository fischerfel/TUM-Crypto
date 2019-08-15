import java.io.*;
import java.math.BigInteger;
import java.net.*;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.RSAPrivateKeySpec;

import javax.crypto.Cipher;

class TCPServer
{
   public static void main(String argv[]) throws Exception
      {
         String clientSentence;
         ServerSocket welcomeSocket = new ServerSocket(23073);

         System.out.println("Waiting");
         while(true)
         {
            Socket connectionSocket = welcomeSocket.accept();
            System.out.println("Client connected");
            DataInputStream dis = new DataInputStream(connectionSocket.getInputStream());

            byte[] arr = null;
            while((arr = readBytes(dis)) != null)
            {
                System.out.println(new String(rsaDecrypt(arr)));
            }

         }
      }
   static public byte[] rsaDecrypt(byte[] data) throws Exception{
       if(TCPServer.privKey == null)
       {
               TCPServer.privKey = readKeyFromFile("private.key");
       }
       Cipher cipher = Cipher.getInstance("RSA");
       cipher.init(Cipher.DECRYPT_MODE, privKey);
       byte[] cipherData = cipher.doFinal(data);
       return cipherData;
     }

static PrivateKey privKey = null;
static PrivateKey readKeyFromFile(String keyFileName) throws IOException {

       ObjectInputStream oin = new ObjectInputStream(new FileInputStream(keyFileName));
       try {
         BigInteger m = (BigInteger) oin.readObject();
         BigInteger e = (BigInteger) oin.readObject();
         RSAPrivateKeySpec keySpec = new RSAPrivateKeySpec(m, e);
         KeyFactory fact = KeyFactory.getInstance("RSA");
         PrivateKey privKey = fact.generatePrivate(keySpec);
         return privKey;
       } catch (Exception e) {
         throw new RuntimeException("Spurious serialisation error", e);
       } finally {
         oin.close();
       }
     }

public static byte[] readBytes(DataInputStream dis) throws IOException {

    int len = dis.readInt();
    byte[] data = new byte[len];
    if (len > 0) {
        dis.readFully(data);
    }
    return data;
}


}
