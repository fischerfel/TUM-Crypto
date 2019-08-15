import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;


public class AESEncrypter {
    public static void encryptToBinaryFile(String password, byte[] bytes, File file) throws EncrypterException {
        try {
            final byte[] rawKey = getRawKey(password.getBytes());
            final FileOutputStream ostream = new FileOutputStream(file, false);

            ostream.write(encrypt(rawKey, bytes));
            ostream.flush();
            ostream.close();

        } catch (IOException e) {
            throw new EncrypterException(e);
        }
    }

public static byte[] decryptFromBinaryFile(String password, File file) throws EncrypterException {
    try {
        final byte[] rawKey = getRawKey(password.getBytes());
        final FileInputStream istream = new FileInputStream(file);
        final byte[] buffer = new byte[(int)file.length()];

        istream.read(buffer);

        return decrypt(rawKey, buffer);

    } catch (IOException e) {
        throw new EncrypterException(e);
    }
}

private static byte[] getRawKey(byte[] seed) throws EncrypterException {
    try {
        final KeyGenerator kgen = KeyGenerator.getInstance("AES");
        final SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");

        sr.setSeed(seed);
        kgen.init(128, sr); // 192 and 256 bits may not be available

        final SecretKey skey = kgen.generateKey();

        return skey.getEncoded();

    } catch (Exception e) {
        throw new EncrypterException(e);
    }
}

private static byte[] encrypt(byte[] raw, byte[] clear) throws EncrypterException {
    try {
        final SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        final Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec);

        return cipher.doFinal(clear);

    } catch (Exception e) {
        throw new EncrypterException(e);
    }
}

private static byte[] decrypt(byte[] raw, byte[] encrypted) throws EncrypterException {
    SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
    try {
        final Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, skeySpec);

        return cipher.doFinal(encrypted);

    } catch (Exception e) {
        throw new EncrypterException(e);
    }
}
