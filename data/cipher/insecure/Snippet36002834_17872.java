import android.util.Base64;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class Decryption {

private SecretKeySpec keySpec;
private String algorithm = "Blowfish/CBC/PKCS5Padding";
private Cipher cipher;

public void setupForDecryption(String key) throws NoSuchPaddingException, NoSuchAlgorithmException, UnsupportedEncodingException {
    byte[] keyData = key.getBytes();
    keySpec = new SecretKeySpec(key.getBytes("UTF-8"), "Blowfish");
    cipher = Cipher.getInstance(algorithm);
}

public boolean checkForKeySpec() {
    if(keySpec !=  null){
        return true;
    }
    return false;
}

public SecretKeySpec getSecretKey() {
    return keySpec;
}

public String decryptString(String inputString) throws InvalidKeyException, BadPaddingException, IllegalBlockSizeException, InvalidAlgorithmParameterException, UnsupportedEncodingException {
    IvParameterSpec ivSpec = new IvParameterSpec(keySpec.getEncoded());
    cipher.init(Cipher.DECRYPT_MODE, getSecretKey(),ivSpec);
    byte[] decryptedBytes = Base64.decode(inputString,0);
    String decrypted = new String(decryptedBytes);
    return decrypted;
}
}
