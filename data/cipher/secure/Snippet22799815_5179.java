import java.io.*;
import java.net.*;
import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;



class TCPClient
{

    public static void main(String argv[]) throws Exception
    {
      String sentence;
      String modifiedSentence;
      BufferedReader inFromUser = new BufferedReader( new InputStreamReader(System.in));
      Socket clientSocket = new Socket("localhost", 6808);
      DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
      BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

      sentence = inFromUser.readLine();

      byte[] raw = new byte[]{'T', 'h', 'i', 's', 'I', 's', 'A', 'S', 'e', 'c', 'r', 'e', 't', 'K', 'e', 'y'};
      SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
      Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
      cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
      byte[] encrypted = cipher.doFinal(sentence.getBytes());
      System.out.println("encrypted string:" + (new String(encrypted)));

      outToServer.writeBytes(new String(encrypted) + '\n');
      modifiedSentence = inFromServer.readLine();
      System.out.println("FROM SERVER: " + modifiedSentence);
      clientSocket.close();
     }
}
