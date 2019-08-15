package fitfast.security;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.KeySpec;
import java.util.Arrays;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public final class Authenticator {

    private static final int length = 512;
    private static final int iterations = 60000;

    public static byte[] generateHash(String password, byte[] salt) throws NoSuchAlgorithmException, InvalidKeySpecException {
        String algorithm = "PBKDF2WithHmacSHA512";
        KeySpec sp = new PBEKeySpec(password.toCharArray(), salt, iterations, length);
        SecretKeyFactory kf = SecretKeyFactory.getInstance(algorithm);
        return kf.generateSecret(sp).getEncoded();
    }

    public static byte[] generateSalt() throws NoSuchAlgorithmException {
        SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
        byte[] salt = new byte[8];
        sr.nextBytes(salt);
        return salt;
    }

    public static boolean check(byte[] hash, String password, byte[] salt) {

        //code goes here

    }

}
