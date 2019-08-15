import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPrivateCrtKeySpec;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.util.ArrayList;
import java.util.List;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.apache.commons.codec.binary.Base64;

public class RsaKeys {
private final String rsaDecAlgorithm = "RSA";
private static KeyFactory kf;
private static Cipher cipher;

public PublicKey publicKey;
public PrivateKey privateKey;
private RsaKeyValue rsaKeyVal = new RsaKeyValue();

public RsaKeys(String path) {
    this.rsaKeyVal = new RsaKeyValue(path);
    this.privateKey = makePrivateKey();
    //this.privateKey = makePrivateKeyTwo();
    this.publicKey = makePublicKey();

}

public String encryptString(byte[] input) {
    try {
        cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        byte[] ret = cipher.doFinal(input);
        return Base64.encodeBase64String(ret);

    } catch (InvalidKeyException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    } catch (IllegalBlockSizeException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    } catch (BadPaddingException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    } catch (NoSuchAlgorithmException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    } catch (NoSuchPaddingException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    }

    return null;

}

public String decrypt(String encryptedString) {
    try {
        cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
    } catch (InvalidKeyException | NoSuchAlgorithmException | NoSuchPaddingException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    }

    int base64BlockSize = (this.rsaKeyVal.getBitStrength() / 8) % 3 != 0
            ? (((this.rsaKeyVal.getBitStrength() / 8) / 3) * 4) + 4
            : (((this.rsaKeyVal.getBitStrength() / 8)) / 3) * 4;

    int iterations = encryptedString.length() / base64BlockSize;
    List<byte[]> temp = new ArrayList<byte[]>();

    /**/
    System.out.println("base64BlockSize: " + base64BlockSize);
    System.out.println("Iterations: " + iterations);
    /**/

    for (int i = 0; i < iterations; i++) {
        try {
            byte[] bytesArray = Base64
                    .decodeBase64(encryptedString.substring(base64BlockSize * i, base64BlockSize));

            /**/
            System.out.println("Byte Array Encrypted String Length: " + bytesArray.length);
            System.out.println("Encoded Byte Array Encrypted String: " + Base64.encodeBase64String((bytesArray)));
            /**/

            temp.add(cipher.doFinal(bytesArray));

        } catch (IllegalBlockSizeException e) {

            e.printStackTrace();
        } catch (BadPaddingException e) {

            e.printStackTrace();
        }
    }

    ByteArrayOutputStream bStr = new ByteArrayOutputStream();
    byte[] retBal = null;
    try {

        temp.stream().forEach(x -> {
            try {
                bStr.write(x);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        });

        retBal = bStr.toByteArray();
    } catch (Exception e) {
        e.printStackTrace();
    } finally {
        try {
            bStr.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    return Base64.encodeBase64String(retBal).toString();
}

public String getPrivateKeyModulus() {
    RSAPrivateKey temp = (RSAPrivateKey) privateKey;
    return "Modulus: " + temp.getModulus();
}

private PublicKey makePublicKey() {

    try {
        byte[] modBytes = Base64.decodeBase64(this.rsaKeyVal.getModulus());
        byte[] expBytes = Base64.decodeBase64(this.rsaKeyVal.getExponent());

        kf = KeyFactory.getInstance(rsaDecAlgorithm);

        BigInteger mod = new BigInteger(1, modBytes);
        BigInteger exp = new BigInteger(1, expBytes);

        RSAPublicKeySpec pubSpec = new RSAPublicKeySpec(mod, exp);
        PublicKey pubKey = kf.generatePublic(pubSpec);

        return pubKey;
    } catch (InvalidKeySpecException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    } catch (NoSuchAlgorithmException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    }

    return null;
}

private PrivateKey makePrivateKey() {

    try {
        byte[] modBytes = Base64.decodeBase64(this.rsaKeyVal.getModulus());
        byte[] dBytes = Base64.decodeBase64(this.rsaKeyVal.getD());

        kf = KeyFactory.getInstance(rsaDecAlgorithm);

        BigInteger mod = new BigInteger(1, modBytes);
        BigInteger d = new BigInteger(1, dBytes);

        RSAPrivateKeySpec privSpec = new RSAPrivateKeySpec(mod, d);
        PrivateKey pKey = kf.generatePrivate(privSpec);

        return pKey;
    } catch (InvalidKeySpecException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    } catch (NoSuchAlgorithmException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    } 

    return null;
}

private PrivateKey makePrivateKeyTwo() {

    byte[] modBytes = Base64.decodeBase64(this.rsaKeyVal.getModulus());
    byte[] expBytes = Base64.decodeBase64(this.rsaKeyVal.getExponent());
    byte[] dBytes = Base64.decodeBase64(this.rsaKeyVal.getD());
    byte[] pBytes = Base64.decodeBase64(this.rsaKeyVal.getP());
    byte[] qBytes = Base64.decodeBase64(this.rsaKeyVal.getQ());
    byte[] dpBytes = Base64.decodeBase64(this.rsaKeyVal.getDp());
    byte[] dqBytes = Base64.decodeBase64(this.rsaKeyVal.getDq());
    byte[] inverseQBytes = Base64.decodeBase64(this.rsaKeyVal.getInverseQ());

    BigInteger mod = new BigInteger(1, modBytes);
    BigInteger exp = new BigInteger(1, expBytes);
    BigInteger d = new BigInteger(1, dBytes);
    BigInteger p = new BigInteger(1, pBytes);
    BigInteger q = new BigInteger(1, qBytes);
    BigInteger dp = new BigInteger(1, dpBytes);
    BigInteger dq = new BigInteger(1, dqBytes);
    BigInteger inverseQ = new BigInteger(1, inverseQBytes);

    RSAPrivateCrtKeySpec keySpec = 
            new RSAPrivateCrtKeySpec(
                    mod,
                    exp,
                    d,
                    p,
                    q,
                    dp,
                    dq,
                    inverseQ
                    );

    try {
        kf = KeyFactory.getInstance(rsaDecAlgorithm);
        PrivateKey key = kf.generatePrivate(keySpec);

        return key;
    } catch (NoSuchAlgorithmException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    } catch (InvalidKeySpecException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    }

    return null;
}
}
