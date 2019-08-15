import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.security.Security;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.crypto.BufferedBlockCipher;

import java.util.Base64;



public class AESdecryptor {
/*
    private static String[] KeyIvEncrypted = new String[]{
            "ABEiM0RVZneImaq7zN3u/w==",
            "AAECAwQFBgcICQoLDA0ODw==",
            "ZtrkahwcMzTu7e/WuJ3AZmF09DE="
            };*/
    public static String[] KeyIvEncrypted = new String[]{
            new String("0123456789abcdef"),
            new String("0000000000000000"),
            new String("1ff4ec7cef0e00d81b2d55a4bfdad4ba")
            };
    public static byte[][] Ivtest = {{0x0,0x1,0x2,0x3,0x4,0x5,0x6,0x7,0x8,0x9,0xa,0xb,0xc,0xd,0xe,0xf},{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},{0x1,0xf,0xf,0x4,0xe,0xc,0x7,0xc,0xe,0xf,0x0,0xe,0x0,0x0,0xd,0x8,0x1,0xb,0x2,0xd,0x5,0x5,0xa,0x4,0xb,0xf,0xd,0xa,0xd,0x4,0xb,0xa}};

    public static void main(String[] args) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, NoSuchProviderException, InvalidAlgorithmParameterException, UnsupportedEncodingException, InvalidKeySpecException{
        Security.addProvider(new BouncyCastleProvider());
        System.out.println(new String(decrypt(),"ISO-8859-1"));



    }

    private static byte[] transform(int mode, byte[] keyBytes, byte[] ivBytes, byte[] messageBytes) throws InvalidKeyException, InvalidAlgorithmParameterException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, NoSuchProviderException, InvalidKeySpecException
    {
        final SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "AES");
        final IvParameterSpec ivSpec = new IvParameterSpec(ivBytes);
        final Cipher cipher = Cipher.getInstance("AES/CBC/pkcs7Padding");
        cipher.init(mode, keySpec, ivSpec);
        return cipher.doFinal(messageBytes);
    }

    public static byte[] decrypt() throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, NoSuchProviderException, InvalidAlgorithmParameterException, UnsupportedEncodingException, InvalidKeySpecException{
         //return AESdecryptor.transform(Cipher.DECRYPT_MODE, Base64.getDecoder().decode(KeyIvEncrypted[0]), Base64.getDecoder().decode(KeyIvEncrypted[1]), Base64.getDecoder().decode(KeyIvEncrypted[2]));
         return AESdecryptor.transform(Cipher.DECRYPT_MODE, Ivtest[0], Ivtest[1], Ivtest[2]);

    }
}
