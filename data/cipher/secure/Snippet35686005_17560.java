package cryptography;

import java.security.AlgorithmParameters;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.InvalidParameterSpecException;
import java.security.spec.KeySpec;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

import java.util.Base64;
import java.util.Base64.Decoder;
import java.util.Base64.Encoder;

public class BasicAESEncrypt {
private final byte[] SALT = {
        (byte) 0xA9, (byte) 0x9B, (byte) 0xC8, (byte) 0x32,
        (byte) 0x56, (byte) 0x35, (byte) 0xE3, (byte) 0x03
    };

private Cipher ecipher;
private Cipher dcipher;
Encoder encoder = Base64.getEncoder();
Decoder decoder = Base64.getDecoder();


BasicAESEncrypt(String password) throws NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, InvalidKeyException, InvalidParameterSpecException, InvalidAlgorithmParameterException{
    SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");

    KeySpec spec = new PBEKeySpec(password.toCharArray(), SALT, 65536, 256);
    SecretKey tmp = factory.generateSecret(spec);        

    SecretKey secret = new SecretKeySpec(tmp.getEncoded(), "AES");

    ecipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
    ecipher.init(Cipher.ENCRYPT_MODE, secret);

    AlgorithmParameters params = ecipher.getParameters();
    byte[] iv = params.getParameterSpec(IvParameterSpec.class).getIV();

    dcipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
    dcipher.init(Cipher.DECRYPT_MODE, secret, new IvParameterSpec(iv));
}

public String encrypt(String encrypt) throws Exception {
    byte[] bytes = encrypt.getBytes("UTF8");
    byte[] encrypted = encrypt(bytes);
    return encoder.encodeToString(encrypted);
}

public byte[] encrypt(byte[] plain) throws Exception {
    return ecipher.doFinal(plain);
}

public String decrypt(String encrypt) throws Exception {
    byte[] decodedData = decoder.decode(encrypt);
    byte[] decrypted = decrypt(decodedData);
    return new String(decrypted, "UTF8");
}

public byte[] decrypt(byte[] encrypt) throws Exception {
    return dcipher.doFinal(encrypt);
}

public static void main(String[] args) throws Exception {
    String message = "Wire message for encryption";
    String password  = "TopSecretKey";

    try {
        BasicAESEncrypt app = new BasicAESEncrypt(password);

        String encrypted = app.encrypt(message);
        System.out.println("Encrypted string is: " + encrypted);

        String decrypted = app.decrypt(encrypted);
        System.out.println("Decrypted string is: " + decrypted);
    } catch (InvalidKeyException | NoSuchAlgorithmException | InvalidKeySpecException | NoSuchPaddingException
            | InvalidParameterSpecException | InvalidAlgorithmParameterException e1) {
        e1.printStackTrace();
    }
    try {
    } catch (Exception e) {
        e.printStackTrace();
    }
}
