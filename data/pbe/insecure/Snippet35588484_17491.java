package il.co.falk;

import javax.crypto.*;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.*;
import java.security.spec.KeySpec;
import java.security.spec.PKCS8EncodedKeySpec;

public class SecureFile {
private PublicKey publicKey;
private byte[] privateKeyArray;
private byte[] salt = {1,2,3,4,5,6,7,8};


public static void main(String[] args) {
    String password = "PASSWORD";
    SecureFile secureFile = new SecureFile(password);
    secureFile.test();
}


public void test() {
    String password = "PASSWORD";
    String imageFile = "348756348975634897562398479623896";

    ImageAndKey imageAndKey = encryptImage(imageFile.getBytes());
    byte[] decryptedImage = decryptImage(imageAndKey, password);

    System.out.println(new String(imageFile));
    System.out.println(new String(decryptedImage));
}

public SecureFile(String password) {
    try {
        generateRSAKeys(password);
    } catch (Exception e) {
        e.printStackTrace();
    }
}



public ImageAndKey encryptImage(byte[] imageBytes) {
    try {
        byte[] secretKeyBytes = generateAESKey();
        byte[] encryptedFile = aesEncrypt(imageBytes, secretKeyBytes);
        byte[] encryptedKey = rsaEncrypt(secretKeyBytes);

        return new ImageAndKey(encryptedFile, encryptedKey);
    } catch (Exception e) {
        e.printStackTrace();
        return null;
    }

}

public byte[] decryptImage(ImageAndKey imageAndKey, String password) {
    try {
        byte[] secretKeyBytes = generateAESKey(password);
        byte[] decryptedPrivateKey = aesDecrypt(privateKeyArray, secretKeyBytes);
        byte[] decryptedKey = rsaDecrypt(imageAndKey.aesKey, decryptedPrivateKey);

        SecretKey secretKey = new SecretKeySpec(decryptedKey, "AES");
        secretKeyBytes = secretKey.getEncoded();

        byte[] decryptedBytes = aesDecrypt(imageAndKey.imageBytes, secretKeyBytes);

        return  decryptedBytes;
    } catch (Exception e) {
        e.printStackTrace();
    }
    return null;
}



// RSA
private void generateRSAKeys(String password) throws Exception {
    final KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
    keyGen.initialize(512); // TODO: make this 2048 at least
    final KeyPair keyPair = keyGen.generateKeyPair();
    publicKey = keyPair.getPublic();
    PrivateKey privateKey = keyPair.getPrivate();

    byte[] secretKeyBytes = generateAESKey(password);
    byte[] privateKeyBytes = privateKey.getEncoded();
    privateKeyArray = aesEncrypt(privateKeyBytes, secretKeyBytes);
}

public byte[] rsaEncrypt(byte[] plainText) throws Exception {
    final Cipher cipher = Cipher.getInstance("RSA");
    cipher.init(Cipher.ENCRYPT_MODE, publicKey);
    byte[] cipherText = cipher.doFinal(plainText);
    return cipherText;
}

public byte[] rsaDecrypt(byte[] cipherText, byte[] decryptedPrivateKeyArray) throws Exception {
    PrivateKey privateKey = KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(decryptedPrivateKeyArray));

    final Cipher cipher = Cipher.getInstance("RSA");
    cipher.init(Cipher.DECRYPT_MODE, privateKey);
    byte[]  plainText = cipher.doFinal(cipherText);
    return plainText;
}

// AES
private byte[] aesEncrypt(byte[] plainText, byte[] secretKeyBytes) throws Exception {
    Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
    cipher.init(Cipher.ENCRYPT_MODE, getSecretKey(secretKeyBytes));
    byte[] cipherText = cipher.doFinal(plainText);
    return cipherText;
}

public byte[] aesDecrypt(byte[] cipherText, byte[] secretKeyBytes) throws Exception {
    Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
    cipher.init(Cipher.DECRYPT_MODE, getSecretKey(secretKeyBytes));
    byte[] plainText = cipher.doFinal(cipherText);
    return plainText;
}

private byte[] generateAESKey() throws Exception {
    KeyGenerator keyGen = KeyGenerator.getInstance("AES");
    keyGen.init(256);
    SecretKey secretKey = keyGen.generateKey();
    return secretKey.getEncoded();
}

private byte[] generateAESKey(String password) throws Exception {
    SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
    KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 65536, 256);
    SecretKey tmp = factory.generateSecret(spec);
    SecretKey secret = new SecretKeySpec(tmp.getEncoded(), "AES");
    return secret.getEncoded();
}

private SecretKey getSecretKey(byte[] secretKeyBytes) throws Exception {
    SecretKey secretKey = new SecretKeySpec(secretKeyBytes, "AES");
    return secretKey;
}



// Classes
class ImageAndKey {
    public byte[] imageBytes;
    public byte[] aesKey;

    public ImageAndKey(byte[] imageBytes, byte[] aesKey) {
        this.imageBytes = imageBytes;
        this.aesKey = aesKey;
    }
}
