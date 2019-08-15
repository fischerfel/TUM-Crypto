package com.myapp.utilities;

import android.content.Context;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Base64;

import java.security.MessageDigest;
import java.security.spec.AlgorithmParameterSpec;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class Security {
    private static final String TAG = Security.class.getSimpleName();

    private Cipher cipher;
    private SecretKeySpec key;
    private AlgorithmParameterSpec spec;

    public Security(Context context) throws Exception {
        // hash password with SHA-256 and crop the output to 128-bit for key
        MessageDigest digest = MessageDigest.getInstance("SHA-256");

        String secret = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
//        secret = "asjldknfajlsdbc DCNJANSDKJNACSDCnmcnc ###";

        digest.update(secret.getBytes("UTF-8"));
        byte[] keyBytes = new byte[32];
        System.arraycopy(digest.digest(), 0, keyBytes, 0, keyBytes.length);

        cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
        key = new SecretKeySpec(keyBytes, "AES");
        spec = getIV();
    }

    public AlgorithmParameterSpec getIV(){
        byte[] iv = {
                (byte) 0x8E, 0x12, 0x39, (byte) 0x9C, 0x07, 0x72, 0x6F, 0x5A,
                (byte) 0x8E, 0x12, 0x39, (byte) 0x9C, 0x07, 0x72, 0x6F, 0x5A
        };
        IvParameterSpec ivParameterSpec;
        ivParameterSpec = new IvParameterSpec(iv);

        return ivParameterSpec;
    }

    public String encrypt(String plainText) throws Exception{
        if (!TextUtils.isEmpty(plainText)) {
            cipher.init(Cipher.ENCRYPT_MODE, key, spec);
            return new String(Base64.encode(cipher.doFinal(plainText.getBytes("UTF-8")), Base64.DEFAULT), "UTF-8");
        }
        return null;
    }

    public String decrypt(String cryptedText) throws Exception {
        if (!TextUtils.isEmpty(cryptedText)) {
            cipher.init(Cipher.DECRYPT_MODE, key, spec);
            return new String(cipher.doFinal(Base64.decode(cryptedText, Base64.DEFAULT)), "UTF-8");
        }
        return null;
    }
}
