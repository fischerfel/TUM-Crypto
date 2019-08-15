
import org.apache.commons.codec.binary.Base64;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

public class AESTest 
{ 
    public static void main(String [] args)
    {
        System.out.println(AESEncryptToBase64("AES/ECB/PKCS5padding", "000000", "XJ5QJSVMKZGBOQO7HMSIJO5BERW2OYWDVNPM3BH32NLSWUCNJ4FIP3BML7EKUBNO"));
        System.out.println(AESEncryptToBase64("AES/CBC/PKCS5padding", "000000", "XJ5QJSVMKZGBOQO7HMSIJO5BERW2OYWDVNPM3BH32NLSWUCNJ4FIP3BML7EKUBNO"));
    }

    /**
     * 
     * @param secret
     * @param cleartext
     * @return encrypted b64 string
     */
    public static String AESEncryptToBase64(String cypher, String secret, String clearText) {
        byte[] rawKey = new byte[32];
        java.util.Arrays.fill(rawKey, (byte) 0);
        byte[] secretBytes = secret.getBytes();
        for(int i = 0; i < secretBytes.length; i++){
            rawKey[i] = secretBytes[i];
        }

        SecretKeySpec skeySpec = new SecretKeySpec(rawKey, "AES");
        try{ 
            Cipher cipher = Cipher.getInstance(cypher);
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
            byte[] encryptedData = cipher.doFinal(clearText.getBytes());
            if(encryptedData == null) return null;
            // return "l";
            return Base64.encodeBase64String(encryptedData);
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;      

    }
}

