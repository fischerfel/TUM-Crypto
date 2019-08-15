import java.io.*;
import java.net.*;
import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.codec.binary.Base64;

class TCPServer
{
   public static void main(String argv[]) throws Exception
      {
         String clientSentence;
         String capitalizedSentence;
         ServerSocket welcomeSocket = new ServerSocket(6808);

         while(true)
         {
            Socket connectionSocket = welcomeSocket.accept();
            BufferedReader inFromClient =
               new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
            DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream());
            clientSentence = inFromClient.readLine();
            System.out.println("Received: " + clientSentence);

            byte[] raw = new byte[]{'T', 'h', 'i', 's', 'I', 's', 'A', 'S', 'e', 'c', 'r', 'e', 't', 'K', 'e', 'y'};
            //SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
            SecretKeySpec skeySpec = new SecretKeySpec(Base64.decodeBase64(raw), "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(skeySpec.getEncoded(),"AES"));
            byte[] original = cipher.doFinal(Base64.decodeBase64(clientSentence));
            original.toString();

            System.out.println("Sent: " + original);

            capitalizedSentence = (new String(original)).toUpperCase() + '\n';
            System.out.println("Sent: " + capitalizedSentence);
            outToClient.writeBytes(capitalizedSentence);
         }
      }
}
