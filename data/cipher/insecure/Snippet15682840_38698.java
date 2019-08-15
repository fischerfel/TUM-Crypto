import java.security.*;
import javax.crypto.*;

// encrypt and decrypt using the DES private key algorithm
public class PrivateExample {

  public static void main (String[] args) throws Exception {
    String text=new String();
    text="This is an encryption test";

    byte[] plainText = text.getBytes("UTF8");

    // get a DES private key
    System.out.println( "\nStart generating DES key" );
    KeyGenerator keyGen = KeyGenerator.getInstance("DES");
    keyGen.init(56);
    Key key = keyGen.generateKey();
    System.out.println( "Finish generating DES key" );
    //
    // get a DES cipher object and print the provider
    Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
    System.out.println( "\n" + cipher.getProvider().getInfo() );
    //
   // encrypt using the key and the plaintext
    System.out.println( "\nStart encryption" );
    cipher.init(Cipher.ENCRYPT_MODE, key);
    byte[] cipherText = cipher.doFinal(plainText);
    System.out.println( "Finish encryption: " );
    System.out.println( new String(cipherText, "UTF8") );

   //Now writing to an ouput file the cipherText
   try{
       FileOutputStream fs=new FileOutputStream("c:/test.txt");
      fs.write(cipherText);
     }catch(Exception e){
       e.printStackTrace();
     }
//How to proceed from here

}
}
