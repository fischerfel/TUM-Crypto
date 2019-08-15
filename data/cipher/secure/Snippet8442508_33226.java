import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class GenerateKeys {

    public static void saveToFile(String fileName, BigInteger mod,
            BigInteger exp) throws IOException {
        ObjectOutputStream oout = new ObjectOutputStream(
                new BufferedOutputStream(new FileOutputStream(fileName)));
        try {
            oout.writeObject(mod);
            oout.writeObject(exp);
        } catch (Exception e) {
            throw new IOException("Unexpected error", e);
        } finally {
            oout.close();
        }
    }

    public static void rsaKeyGeneration() {
        KeyPairGenerator kpg = null;
        try {
            kpg = KeyPairGenerator.getInstance("RSA");
        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        kpg.initialize(1024);
        KeyPair kp = kpg.genKeyPair();
        Key publicKey = kp.getPublic();
        Key privateKey = kp.getPrivate();
        System.out.println("Algo is :" + publicKey.getAlgorithm());

        KeyFactory fact = null;
        try {
            fact = KeyFactory.getInstance("RSA");
        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        RSAPublicKeySpec pub = null;
        try {
            pub = fact.getKeySpec(publicKey, RSAPublicKeySpec.class);
        } catch (InvalidKeySpecException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        RSAPrivateKeySpec priv = null;
        try {
            priv = fact.getKeySpec(privateKey, RSAPrivateKeySpec.class);
        } catch (InvalidKeySpecException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        try {
            saveToFile("C:/public1.key", pub.getModulus(),
                    pub.getPublicExponent());
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        try {
            saveToFile("C:/private1.key", priv.getModulus(),
                    priv.getPrivateExponent());
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    PublicKey readKeyFromFile(String keyFileName) throws IOException {
        InputStream in = new FileInputStream(keyFileName);
        ObjectInputStream oin = new ObjectInputStream(new BufferedInputStream(
                in));

        try {
            BigInteger m = (BigInteger) oin.readObject();
            BigInteger e = (BigInteger) oin.readObject();
            RSAPublicKeySpec keySpec = new RSAPublicKeySpec(m, e);
            KeyFactory fact = KeyFactory.getInstance("RSA");
            PublicKey pubKey = fact.generatePublic(keySpec);
            return pubKey;
        } catch (Exception e) {
            throw new RuntimeException("Spurious serialisation error", e);
        } finally {
            oin.close();
        }
    }

    public byte[] rsaEncrypt(byte[] data) {
        PublicKey pubKey = null;
        try {
            pubKey = readKeyFromFile("C:/public1.key");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        Cipher cipher = null;
        try {
            cipher = Cipher.getInstance("RSA");
        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        try {
            cipher.init(Cipher.ENCRYPT_MODE, pubKey);
        } catch (InvalidKeyException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        byte[] cipherData = null;
        try {
            cipherData = cipher.doFinal(data);
        } catch (IllegalBlockSizeException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (BadPaddingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        System.out
                .println("encrypted card number is :" + cipherData.toString());
        return cipherData;
    }

    PrivateKey readPriKeyFromFile(String keyFileName) throws IOException {
        InputStream in = new FileInputStream(keyFileName);
        ObjectInputStream oin = new ObjectInputStream(new BufferedInputStream(
                in));

        try {
            BigInteger m = (BigInteger) oin.readObject();
            BigInteger e = (BigInteger) oin.readObject();
            RSAPrivateKeySpec keySpec = new RSAPrivateKeySpec(m, e);
            KeyFactory fact = KeyFactory.getInstance("RSA");
            PrivateKey pKey = fact.generatePrivate(keySpec);
            return pKey;
        } catch (Exception e) {
            throw new RuntimeException("Spurious serialisation error", e);
        } finally {
            oin.close();
        }
    }

    public byte[] rsaDecrypt(byte[] sampleText) {
        PrivateKey priKey = null;
        try {
            priKey = readPriKeyFromFile("C:/private1.key");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        Cipher cipher = null;
        try {
            cipher = Cipher.getInstance("RSA");
        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        try {
            cipher.init(Cipher.DECRYPT_MODE, priKey);
        } catch (InvalidKeyException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        byte[] cipherData = null;
        try {
            cipherData = cipher.doFinal(sampleText);
            // cipherData = cipher.
        } catch (IllegalBlockSizeException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (BadPaddingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return cipherData;
    }

    public static void main(String s[]) {
        System.out
                .println("++++++++++++++++++++ Program to create the Keys+++++++++++++++++++++++++++++");
        rsaKeyGeneration();
        String sText = "HRB";
        System.out.println("Plain Text Card Number :" + sText);
        GenerateKeys keys = new GenerateKeys();
        byte[] encryptedCardNo = keys.rsaEncrypt(sText.getBytes());
        byte[] decryptedCardNo = keys.rsaDecrypt(encryptedCardNo);
        System.out.println("Decrypted card number is :"
                + decryptedCardNo.toString());

    }
}
