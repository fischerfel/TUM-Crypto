import java.net.*; 
import java.io.*; 
import javax.crypto.*;
import javax.crypto.spec.*;
import java.security.*;
import sun.misc.*;
import java.io.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.lang.Exception;

public class Server { 
  public static void main (String [] args ) throws IOException { 
    ServerSocket serverSocket = new ServerSocket(15123); 
    Socket socket = serverSocket.accept(); 
    System.out.println("Accepted connection : " + socket); 

    System.out.println("A Simple Program using RSA to encrypt a single symmetric key using");
    System.out.println("Advanced Encryption Standard (AES).\n");
    System.out.println("Generating a symmetric (AES) key...");
    try{
      KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
      keyGenerator.init(128);
      Key AESKey = keyGenerator.generateKey();
      System.out.println("Format: "+AESKey.getFormat());
    } catch (Exception e){
      System.out.println("WRONG!");
    }

    // Create an RSA key pair
    System.out.println("Generating an RSA key...");
    try{
      KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
      keyPairGenerator.initialize(1024);
      // Insert your statement here
      KeyPair keyPair = keyPairGenerator.genKeyPair();
      System.out.println(keyPair.getPublic().getFormat());
      System.out.println(keyPair.getPrivate().getFormat());
      String publicKeyFilename = "public";

      byte[] publicKeyBytes = keyPair.getPublic().getEncoded();

      X509EncodedKeySpec x509Encoded = new X509EncodedKeySpec(publicKeyBytes);
      FileOutputStream fos = new FileOutputStream(publicKeyFilename);
      fos.write(x509Encoded.getEncoded());
      fos.close();

      String privateKeyFilename = "privateKeyFilename";

      byte[] privateKeyBytes = keyPair.getPrivate().getEncoded();

      //byte[] encryptedPrivateKeyBytes = passwordEncrypt(password.toCharArray(), privateKeyBytes);
      PKCS8EncodedKeySpec pkcs8Encoded = new PKCS8EncodedKeySpec(privateKeyBytes);
      fos = new FileOutputStream(privateKeyFilename);
      fos.write(pkcs8Encoded.getEncoded());
      fos.close();

      System.out.println("Done generating the key.\n");
    } catch (Exception e){
      System.out.println("WRONG!");
    }

    // Initialise the RSA cipher with PUBLIC key
    Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
    // Insert your statement here
    cipher.init(Cipher.ENCRYPT_MODE, keyPair.getPublic());

    // Get the bytes of the AES key
    byte[] encryptedKey = AESKey.getEncoded();
    System.out.println("AES key:\n" + asHex(encryptedKey) + "\n");

    // Perform the actual encryption on those bytes
    byte[] cipherText = cipher.doFinal(encryptedKey);
    System.out.println("Encrypted key:\n" + asHex(cipherText) + "\n");

    File transferFile = new File ("copy.doc"); 
    byte [] bytearray = new byte [(int)transferFile.length()]; 
    FileInputStream fin = new FileInputStream(transferFile); 
    BufferedInputStream bin = new BufferedInputStream(fin); 
    bin.read(bytearray,0,bytearray.length); 
    OutputStream os = socket.getOutputStream(); 
    System.out.println("Sending Files..."); 
    os.write(bytearray,0,bytearray.length); 
    os.flush(); 
    socket.close(); 
    System.out.println("File transfer complete"); 
  } 

  public static String asHex (byte buf[]) {

    //Obtain a StringBuffer object
    StringBuffer strbuf = new StringBuffer(buf.length * 2);
    int i;

    for (i = 0; i < buf.length; i++) {
      if (((int) buf[i] & 0xff) < 0x10)
        strbuf.append("0");
      strbuf.append(Long.toString((int) buf[i] & 0xff, 16));
    }
    // Return result string in Hexadecimal format
    return strbuf.toString();
  }
}
