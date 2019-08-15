import java.io.FileOutputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;



//import org.apache.log4j.Logger;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.encoders.Base64;


public class AES_Encrypt_Decrypt_Bouncy_Castle {

    public static SecretKeySpec getKey() {
        try {
            String keyFromfile = "ZoyNMZzsOYh7BxwcgJseBji95hmVTlgKe/9KFY44Jzg=";

            byte[] keyBytes = Base64.decode(keyFromfile.getBytes());
            byte[] finalKeyBytes = new byte[32];

            for (int i = 0; i < 32; i++) {
                finalKeyBytes[i] = keyBytes[i];
            }
            System.out.println("keyBytes="+keyBytes.length);
            System.out.println("Key is "+ keyBytes);
            for(int j=32;j<keyBytes.length;j++){
                System.out.println("keyBytes="+keyBytes[j]);
            }           
            SecretKeySpec skeySpec = new SecretKeySpec(finalKeyBytes, "AES");
            return skeySpec;
        } catch (Exception e) {
            return null;
        }

    }


    public static void main(String[] args) throws Exception {

        SecretKeySpec skeySpec = getKey();
        byte [] iv = Base64.decode("AWNCK2F/9llI0Rs+dQB36Q==");
        IvParameterSpec initializationVector = new IvParameterSpec(iv);
        Cipher cipher = Cipher.getInstance("AES/CBC/ISO10126Padding", new BouncyCastleProvider());  
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec, initializationVector);
        String message = "Brian12345678";
        byte[] encrypted = cipher.doFinal(message.getBytes());
        System.out.println("Decoded plain text = "+new String (encrypted));
        byte[] encodedEncryptedText = Base64.encode(encrypted);
        System.out.println("encrypted string: " + new String (encodedEncryptedText));
        Cipher cipher2 = Cipher.getInstance("AES/CBC/ISO10126Padding", new BouncyCastleProvider());
        cipher2.init(Cipher.DECRYPT_MODE, skeySpec,initializationVector);
        byte[] original = cipher2.doFinal(Base64.decode(encodedEncryptedText));
        String originalString = new String(original);
        System.out.println("Original string: " + originalString);

    }

}
