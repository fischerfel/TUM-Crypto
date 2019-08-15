package tripledes;

import javax.crypto.*;
import javax.crypto.spec.*;
import java.security.*;
import java.security.spec.*;
import java.io.*;

public class TripleDES {

public static void main(String[] args) {

    try {
        try {
            Cipher c = Cipher.getInstance("DESede");
        } catch (Exception e) {
            System.err.println("Installing SunJCE provicer");
            Provider sunjce = new com.sun.crypto.provider.SunJCE();
            Security.addProvider(sunjce);
        }

        File keyfile = new File(args[1]);

        if (args[0].equals("-g")) {
            System.out.println("Generating key. This may take some time...");
            System.out.flush();
            SecretKey key = generateKey();
            writeKey(key, keyfile);
            System.out.println("Done");
            System.out.println("Secret key written to " + args[1] + ". Protect that file!");
        } else if (args[0].equals("-e")) {
            SecretKey key = readKey(keyfile);
            encrypt(key, System.in, System.out);
        } else if (args[0].equals("-d")) {
            SecretKey key = readKey(keyfile);
            decrypt(key, System.in, System.out);
        }
    } catch (Exception e) {
        System.err.println(e);
        System.err.println("Usage: java " + TripleDES.class.getName() + "-d|-e|-g <keyfile>");
    }

}

public static SecretKey generateKey() throws NoSuchAlgorithmException {
    KeyGenerator keygen = KeyGenerator.getInstance("DESede");
    return keygen.generateKey();
}

public static void writeKey(SecretKey key, File f) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
    SecretKeyFactory keyfactory = SecretKeyFactory.getInstance("DESede");
    DESedeKeySpec keyspec = (DESedeKeySpec) keyfactory.getKeySpec(key, DESedeKeySpec.class);
    byte[] rawkey = keyspec.getKey();

    FileOutputStream out = new FileOutputStream(f);
    out.write(rawkey);
    out.close();
}

public static SecretKey readKey(File f) throws IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidKeySpecException {
    DataInputStream in = new DataInputStream(new FileInputStream(f));
    byte[] rawkey = new byte[(int) f.length()];
    in.readFully(rawkey);
    in.close();

    DESedeKeySpec keyspec = new DESedeKeySpec(rawkey);
    SecretKeyFactory keyfactory = SecretKeyFactory.getInstance("DESede");
    SecretKey key = keyfactory.generateSecret(keyspec);
    return key;
}

public static void encrypt(SecretKey key, InputStream in, OutputStream out)
        throws NoSuchAlgorithmException, InvalidKeyException, NoSuchPaddingException, IOException {
    Cipher cipher = Cipher.getInstance("DESede");
    cipher.init(Cipher.ENCRYPT_MODE, key);

    CipherOutputStream cos = new CipherOutputStream(out, cipher);

    byte[] buffer = new byte[2048];
    int bytesRead;
    while ((bytesRead = in.read(buffer)) != -1) {
        cos.write(buffer, 0, bytesRead);
    }
    cos.close();

    java.util.Arrays.fill(buffer, (byte) 0);
}

public static void decrypt(SecretKey key, InputStream in, OutputStream out)
        throws NoSuchAlgorithmException, InvalidKeyException, IOException, IllegalBlockSizeException,
        NoSuchPaddingException, BadPaddingException {
    Cipher cipher = Cipher.getInstance("DESede");
    cipher.init(Cipher.DECRYPT_MODE, key);

    byte[] buffer = new byte[2048];
    int bytesRead;
    while ((bytesRead = in.read(buffer)) != -1) {
        out.write(cipher.update(buffer, 0, bytesRead));
    }
    out.write(cipher.doFinal());
    out.flush();
}

}
