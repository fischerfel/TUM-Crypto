package com.citc.testencryption;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

public class Main extends Activity {

    public static final int SALT_LENGTH = 20;
    public static final int PBE_ITERATION_COUNT = 1000;

    private static final String RANDOM_ALGORITHM = "SHA1PRNG";
    private static final String PBE_ALGORITHM = "PBEWithSHA256And256BitAES-CBC-BC";
    private static final String CIPHER_ALGORITHM = "AES/CBC/PKCS5Padding";

    private static final String TAG = Main.class.getSimpleName();

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        try {

            String password = "password";
            String plainText = "plaintext message to be encrypted";

            // byte[] salt = generateSalt();
            byte[] salt = "dfghjklpoiuytgftgyhj".getBytes();
            Log.i(TAG, "Salt: " + salt.length + " " + HexEncoder.toHex(salt));
            PBEKeySpec pbeKeySpec = new PBEKeySpec(password.toCharArray(), salt, PBE_ITERATION_COUNT, 256);
            SecretKeyFactory factory = SecretKeyFactory.getInstance(PBE_ALGORITHM);
            SecretKey tmp = factory.generateSecret(pbeKeySpec);
            SecretKey secret = new SecretKeySpec(tmp.getEncoded(), "AES");
            byte[] key = secret.getEncoded();
            Log.i(TAG, "Key: " + HexEncoder.toHex(key));

            // PBEParameterSpec pbeParamSpec = new PBEParameterSpec(salt, ITERATION_COUNT);

            Cipher encryptionCipher = Cipher.getInstance(CIPHER_ALGORITHM);

            // byte[] encryptionSalt = generateSalt();
            // Log.i(TAG, "Encrypted Salt: " + encryptionSalt.length + " " + HexEncoder.toHex(encryptionSalt));
            // PBEParameterSpec pbeParamSpec = new PBEParameterSpec(encryptionSalt, 1000);
            // byte[] iv = params.getParameterSpec(IvParameterSpec.class).getIV();
            Log.i(TAG, encryptionCipher.getParameters() + " ");
            byte[] iv = generateIv();
            IvParameterSpec ivspec = new IvParameterSpec(iv);

            encryptionCipher.init(Cipher.ENCRYPT_MODE, secret, ivspec);
            byte[] encryptedText = encryptionCipher.doFinal(plainText.getBytes());
            Log.i(TAG, "Encrypted: " + HexEncoder.toHex(encryptedText));

            Cipher decryptionCipher = Cipher.getInstance(CIPHER_ALGORITHM);
            decryptionCipher.init(Cipher.DECRYPT_MODE, secret, ivspec);
            byte[] decryptedText = decryptionCipher.doFinal(encryptedText);
            Log.i(TAG, "Decrypted: " + new String(decryptedText));

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private byte[] generateSalt() throws NoSuchAlgorithmException {
        SecureRandom random = SecureRandom.getInstance(RANDOM_ALGORITHM);
        byte[] salt = new byte[SALT_LENGTH];
        random.nextBytes(salt);
        return salt;
    }

    private byte[] generateIv() throws NoSuchAlgorithmException {
        SecureRandom random = SecureRandom.getInstance(RANDOM_ALGORITHM);
        byte[] iv = new byte[16];
        random.nextBytes(iv);
        return iv;
    }

}
