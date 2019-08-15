/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package publicprivatekey;

import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import javax.crypto.*;

/**
 *
 * @author Rajorshi
 */
public class PublicPrivateKey {

    public static String getEncrypted(String data, String Key) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidKeySpecException, IllegalBlockSizeException, BadPaddingException {
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        PublicKey publicKey = KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(Base64.getDecoder().decode(Key.getBytes())));
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        byte[] encryptedbytes = cipher.doFinal(data.getBytes());
        return new String(Base64.getEncoder().encode(encryptedbytes));
    }

    public static String getDecrypted(String data, String Key) throws NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        PrivateKey pk = KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(Base64.getDecoder().decode(Key.getBytes())));
        cipher.init(Cipher.DECRYPT_MODE, pk);
        byte[] encryptedbytes = cipher.doFinal(Base64.getDecoder().decode(data.getBytes()));
        return new String(encryptedbytes);
    }

    public static void main(String[] args) throws NoSuchAlgorithmException, NoSuchProviderException, InvalidKeySpecException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        // TODO code application logic here
        KeyGenerator keyGenerator = KeyGenerator.getInstance("Blowfish");
        keyGenerator.init(448);
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(1024);
        KeyPair keyPair = keyPairGenerator.genKeyPair();

        String pubKey = new String(Base64.getEncoder().encode(keyPair.getPublic().getEncoded()));
        String priKey = new String(Base64.getEncoder().encode(keyPair.getPrivate().getEncoded()));
        System.out.println("Public Key:" + pubKey);
        System.out.println("Private Key:" + priKey);
        String cipherText = getEncrypted("hi this is a string", pubKey);

        System.out.println("CHIPHER:" + cipherText);
        String decryptedText = getDecrypted(cipherText, priKey);
        System.out.println("DECRYPTED STRING:" + decryptedText);

    }

}
