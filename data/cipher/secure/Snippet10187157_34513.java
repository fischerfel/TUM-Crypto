import java.io.*;
import java.security.MessageDigest;
import java.security.Security;
import java.util.Arrays;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
public class NewKey{
public static void main(String[] args) throws Exception {
    Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
    String username = "username@teleparadigm.org";
    String userdata = "depression"+" "+"headache";
    // Get the Key
    byte[] key = (username).getBytes();
    MessageDigest sha = MessageDigest.getInstance("SHA-256");
    key = sha.digest(key);
    key = Arrays.copyOf(key, 16); 

    SecretKeySpec secretKeySpec = new SecretKeySpec(key, "AES");    
    System.out.println("key used is "+username);
    System.out.println("encrypted key used is "+key);


    Cipher cipher = Cipher.getInstance("AES/CTR/NoPadding", "BC");
    final byte[] IV = {-85, -67, -5, 88, 28, 49, 49, 85,114, 83, -40, 119, -65, 91, 76, 108};// Hard coded for now
    final IvParameterSpec ivSpec = new IvParameterSpec(IV);

    System.out.println("Derived AES key is: " +secretKeySpec.toString().getBytes().length );

    cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec,ivSpec);
    byte[] encrypted = cipher.doFinal((userdata).getBytes());                                              
    System.out.println("encrypted userdata: " + encrypted);

    cipher.init(Cipher.DECRYPT_MODE, secretKeySpec,ivSpec);
    byte[] original = cipher.doFinal(encrypted);
    String originalString = new String(original);
    System.out.println("Original userdata: " + originalString );
}
}
