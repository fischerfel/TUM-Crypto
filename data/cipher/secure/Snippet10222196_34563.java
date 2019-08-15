import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.xml.bind.DatatypeConverter;

public class RSAExample {
    private static byte[] h2b(String hex){
        return DatatypeConverter.parseHexBinary(hex);
    }
    private static String b2h(byte[] bytes){
        return DatatypeConverter.printHexBinary(bytes);
    }

    private static SecureRandom sr = new SecureRandom();

    public static KeyPair newKeyPair(int rsabits) throws NoSuchAlgorithmException {
        KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
        generator.initialize(rsabits, sr);
        return generator.generateKeyPair();
    }

    public static byte[] pubKeyToBytes(PublicKey key){
        return key.getEncoded(); // X509 for a public key
    }
    public static byte[] privKeyToBytes(PrivateKey key){
        return key.getEncoded(); // PKCS8 for a private key
    }

    public static PublicKey bytesToPubKey(byte[] bytes) throws InvalidKeySpecException, NoSuchAlgorithmException{
        return KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(bytes));
    }
    public static PrivateKey bytesToPrivKey(byte[] bytes) throws InvalidKeySpecException, NoSuchAlgorithmException{
        return KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(bytes));
    }

    public static byte[] encryptWithPubKey(byte[] input, PublicKey key) throws IllegalBlockSizeException, BadPaddingException, InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException {
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.ENCRYPT_MODE, key);
        return cipher.doFinal(input);
    }
    public static byte[] decryptWithPrivKey(byte[] input, PrivateKey key) throws IllegalBlockSizeException, BadPaddingException, InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException {
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.DECRYPT_MODE, key);
        return cipher.doFinal(input);
    }


    public static void main(String[] args) throws Exception {
        KeyPair kp = newKeyPair(1<<11); // 2048 bit RSA; might take a second to generate keys
        PublicKey pubKey = kp.getPublic();
        PrivateKey privKey = kp.getPrivate();
        String plainText = "Dear Bob,\nWish you were here.\n\t--Alice";
        byte[] cipherText = encryptWithPubKey(plainText.getBytes("UTF-8"),pubKey);
        System.out.println("cipherText: "+b2h(cipherText));
        System.out.println("plainText:");
        System.out.println(new String(decryptWithPrivKey(cipherText,privKey),"UTF-8"));
    }
}
