import java.security.*;
import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import sun.misc.*;
import org.apache.commons.codec.binary.Hex;

public class AESencrp {

    private static final String ALGO = "AES/CBC/PKCS5Padding";


    private static final byte[] keyValue = "MyEncryptionKey1".getBytes();
    private static Key key;
    private static Cipher decryptor;
    private static Cipher encryptor;


    public static  void init() throws Exception 
    {

        key = generateKey();
        encryptor = Cipher.getInstance(ALGO);
        IvParameterSpec iv = new IvParameterSpec(Hex.decodeHex("12345678901234567890123456789012".toCharArray()));   
        decryptor=Cipher.getInstance(ALGO);
        encryptor.init(Cipher.ENCRYPT_MODE, key,iv);
        decryptor.init(Cipher.DECRYPT_MODE, key,iv);
    }




public static String encrypt(String Data) throws Exception {
        byte[] encVal = encryptor.doFinal(Data.getBytes());
        String encryptedValue = new BASE64Encoder().encode(encVal);
        return encryptedValue;
    }

 public static String decrypt(String encryptedData) throws Exception {

        byte[] decordedValue = new BASE64Decoder().decodeBuffer(encryptedData);
        byte[] decValue = decryptor.doFinal(decordedValue);
        String decryptedValue = new String(decValue);
        return decryptedValue;
    }

 private static Key generateKey() throws Exception {
        if (key==null)
        key = new SecretKeySpec(keyValue, "AES");
        return key;
}

  public static void main(String[] args) throws Exception {

        init();
        String password = "mypassword";
        String passwordEnc = AESencrp.encrypt(password);
        String passwordDec = AESencrp.decrypt(passwordEnc);

        System.out.println("Plain Text : " + password);
        System.out.println("Encrypted Text : " + passwordEnc);
        System.out.println("Decrypted Text : " + passwordDec);
    }
    }
