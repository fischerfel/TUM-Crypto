package nsdl.crypto;

import java.io.*;
import java.security.spec.*;
import javax.crypto.*;
import javax.crypto.spec.*;

public class BlockCrypt {
Cipher ecipher;
Cipher dcipher;
byte[] keyBytes;
byte[] ivBytes;
SecretKey key;
AlgorithmParameterSpec iv;
byte[] buf = new byte[1024];

BlockCrypt(String keyStr, String ivStr, String algorithm, String mode) {
    try {
        ecipher = Cipher.getInstance(algorithm + "/" + mode + "/PKCS5Padding");
        dcipher = Cipher.getInstance(algorithm + "/" + mode + "/PKCS5Padding");

        keyBytes = hexStringToByteArray(keyStr);
        ivBytes = hexStringToByteArray(ivStr);

        key = new SecretKeySpec(keyBytes, algorithm);
        iv = new IvParameterSpec(ivBytes);

        ecipher.init(Cipher.ENCRYPT_MODE, key, iv);
        dcipher.init(Cipher.DECRYPT_MODE, key, iv);
    } catch (Exception e) {
        System.err.println(e.getMessage());
    }
}

public void encrypt(InputStream in, OutputStream out) {
    try {
        // out: where the plaintext goes to become encrypted
        out = new CipherOutputStream(out, ecipher);

        // in: where the plaintext comes from
        int numRead = 0;
        while ((numRead = in.read(buf)) >= 0) {
            out.write(buf, 0, numRead);
        }
        out.close();
    } catch (IOException e) {
        System.err.println(e.getMessage());
    }
}

public void decrypt(InputStream in, OutputStream out) {
    try {
        // in: where the plaintext come from, decrypted on-the-fly
        in = new CipherInputStream(in, dcipher);

        // out: where the plaintext goes
        int numRead = 0;
        while ((numRead = in.read(buf)) >= 0) {
            out.write(buf, 0, numRead);
        }
        out.flush();
        out.close();
    } catch (IOException e) {
        System.err.println(e.getMessage());
    }
}
public static byte[] hexStringToByteArray(String s) {
    int len = s.length();
    byte[] data = new byte[len / 2];
    for (int i = 0; i < len; i += 2) {
        data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
        + Character.digit(s.charAt(i+1), 16));
    }
    return data;
}
}
