import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;


public class Encryption {

private String algorithm = "Blowfish/CBC/PKCS5Padding";
private SecretKeySpec keySpec;
private Cipher cipher;


public void setupForEncryption(String keyString) throws java.security.GeneralSecurityException, UnsupportedEncodingException {
    byte[] keyData = keyString.getBytes();
    keySpec = new SecretKeySpec(keyString.getBytes("UTF-8"), "Blowfish");
    cipher = Cipher.getInstance(algorithm);
}

public SecretKeySpec getSecretKey() {
    return keySpec;
}

public boolean checkForKeySpec() {
    if (keySpec != null) {
        return true;
    }
    return false;
}

public String encryptString(String inputString) throws java.security.GeneralSecurityException, UnsupportedEncodingException {
    IvParameterSpec ivSpec = new IvParameterSpec(keySpec.getEncoded());
    cipher.init(Cipher.ENCRYPT_MODE, getSecretKey(),ivSpec);
    byte[] encryptedBytes = cipher.doFinal(inputString.getBytes("UTF-8"));
    return new String(encryptedBytes);
}
