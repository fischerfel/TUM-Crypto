import de.flexiprovider.api.keys.PrivateKey;
import de.flexiprovider.api.keys.PublicKey;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class RSA {

private String str, s;
private String chipertext;
private byte[] cipherData;


public RSA(String string) throws Exception {

    try {


        String input = string;
        FileReader read = new FileReader(input);
        BufferedReader reader = new BufferedReader(read);
        while ((s = reader.readLine()) != null) {
        byte[] theByteArray = s.getBytes();
           setUserinput(string);
            rsHex(theByteArray);
}


    } catch (Exception ex) {
        Logger.getLogger(RSA.class.getName()).log(Level.SEVERE, null, ex);
    }

     //Creating an RSA key pair in Java
            KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA"); //instance of KeyPairGenerator
            kpg.initialize(1024);//bit length of the modulus that  required
            KeyPair kp = kpg.genKeyPair();//returns a KeyPair object
            Key publicKey = kp.getPublic(); //pull out the public and private keys
            Key privateKey = kp.getPrivate();

            //Saving the public and private key
            //private key will be placed on our server, and the public key distributed to clients.
            KeyFactory fact = KeyFactory.getInstance("RSA");
            RSAPublicKeySpec pub = (RSAPublicKeySpec) fact.getKeySpec(publicKey, RSAPublicKeySpec.class);
            RSAPrivateKeySpec priv = (RSAPrivateKeySpec) fact.getKeySpec(privateKey, RSAPrivateKeySpec.class);

            // Save the file to local drive
            saveToFile("c:\\public.key", pub.getModulus(), pub.getPublicExponent());
            saveToFile("c:\\private.key", priv.getModulus(),priv.getPrivateExponent());

     }
private void rsHex(byte[] bytes) throws Exception {
    StringBuilder hex = new StringBuilder();
    for (byte b : bytes) {
        String hexString = Integer.toHexString(0x00FF & b);
        hex.append(hexString.length() == 1 ? "0" + hexString : hexString);
    }
 setChipertext(hex.toString());
 }

//save the moduli and exponents to file, we can just use boring old serialisation
public void saveToFile(String fileName, BigInteger mod, BigInteger exp) throws  IOException {
    FileOutputStream f = new FileOutputStream(fileName);
    ObjectOutputStream oos = new ObjectOutputStream(f);
    oos.writeObject(mod);
    oos.writeObject(exp);
    oos.close();
 }

////Encryption
//initialise the cipher with the public key that we previously saved to file.
   PublicKey readKeyFromFile(String keyFileName) throws IOException {
  PublicKey key = null;

  try {
    FileInputStream fin = new FileInputStream(keyFileName);
    ObjectInputStream ois = new ObjectInputStream(fin);
    BigInteger m = (BigInteger) ois.readObject();
    BigInteger e = (BigInteger) ois.readObject();
    RSAPrivateKeySpec keySpec = new RSAPrivateKeySpec(m, e);
    KeyFactory fact = KeyFactory.getInstance("RSA");
        java.security.PublicKey pubKey = fact.generatePublic(keySpec);
    ois.close();
  }
  catch (Exception e) {
   e.printStackTrace();
  }
 return key;
   } 


public void rsaEncrypt(String str)throws Exception {

PublicKey pubKey = readKeyFromFile(str);
  Cipher cipher = Cipher.getInstance("RSA");
    cipher.init(Cipher.ENCRYPT_MODE, pubKey);//initialise the cipher
    cipherData = cipher.doFinal(str.getBytes());//passing in the data to be encrypted


    rsHex(cipherData);


  }


 public String getUserinput() {
    return str;
  }

  public String getChipertext() {
     return chipertext;
  }

  public void setUserinput(String input) {
    this.str = input;
  }

  public void setChipertext(String chipertext) throws Exception {
    this.chipertext = chipertext;

   }



  }


     ----main Program------
  import java.util.Scanner;

 public class TWO{
  public static void main(String[] args) throws Exception{


    Scanner scan = new Scanner(System.in);

    System.out.println("Insert your string");
    String str = scan.nextLine();

            RSA two = new RSA(str);


    System.out.println("Encrypted: "+ two.getChipertext());



}
}
