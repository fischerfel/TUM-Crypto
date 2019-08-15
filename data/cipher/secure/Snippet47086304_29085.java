    package com.company;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.*;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

public class aystmmetricEncryption {

    static PrivateKey privateKey;
    static PublicKey publicKey;

    public static void generateKeys() throws Exception {

        Map<String, Object> keys = getRSAKeys();
        privateKey = (PrivateKey) keys.get("private");
        publicKey = (PublicKey) keys.get("public");

    }

    private static Map<String, Object> getRSAKeys() throws Exception {

        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2048);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        PrivateKey privateKey = keyPair.getPrivate();
        PublicKey publicKey = keyPair.getPublic();
        Map<String, Object> keys = new HashMap<String, Object>();
        keys.put("private", privateKey);
        keys.put("public", publicKey);

        return keys;

    }

    public static String encryptMessage(String plainText, PublicKey publicKey) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {

        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);

        return Base64.getEncoder().encodeToString(cipher.doFinal(plainText.getBytes()));

    }

    public static String decryptMessage(PrivateKey privateKey, String encryptedText) throws BadPaddingException, IllegalBlockSizeException, InvalidKeyException, NoSuchPaddingException, NoSuchAlgorithmException {

        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, privateKey);

        return new String(cipher.doFinal(Base64.getDecoder().decode(encryptedText)));

    }

    public PublicKey getPublicKey() {

        return publicKey;

    }

    public PrivateKey getPrivateKey() {

        return privateKey;

    }
}
