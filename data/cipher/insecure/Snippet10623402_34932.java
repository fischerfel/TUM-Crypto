import java.security.spec.KeySpec;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;

public class TripleDes2 {

private static final String UNICODE_FORMAT = "UTF8";
private static final String DESEDE_ENCRYPTION_SCHEME = "DESede";
private static final String CIPHER_ALG = "DESede/ECB/Nopadding"; //assuming no padding
private KeySpec ks;
private SecretKeyFactory skf;
private Cipher cipher;
private byte[] arrayBytes;
private String myEncryptionKey;
private SecretKey key;

public static void main(String args []) throws Exception {
    TripleDes2 td= new TripleDes2();

    String decrypted = td.decrypt("AC9C5A46A63FC9EA");
    System.out.println("expecting: 04286EDDFDEA6BD7");
    System.out.println("found: " + decrypted);
}

public TripleDes2() throws Exception {
    myEncryptionKey = "1032FD2CD64A9D7FA4D061F76B04BFEA";
    arrayBytes = myEncryptionKey.getBytes(UNICODE_FORMAT);
    ks = new DESedeKeySpec(arrayBytes);
    skf = SecretKeyFactory.getInstance(DESEDE_ENCRYPTION_SCHEME);

    cipher = Cipher.getInstance(CIPHER_ALG);
    key = skf.generateSecret(ks);
}

public String decrypt(String encryptedString) {
    String decryptedText=null;
    try {
        cipher.init(Cipher.DECRYPT_MODE, key);
        byte[] encryptedText = encryptedString.getBytes();
        byte[] plainText = cipher.doFinal(encryptedText);
        decryptedText= new String(plainText);
    } catch (Exception e) {
        e.printStackTrace();
    }
    return decryptedText;
}
}
