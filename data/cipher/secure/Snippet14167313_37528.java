import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import java.io.*;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

public class Test {

    /**
     * Generate an RSA key pair and save it to the file
     */
    public static KeyPair genKeys(File keyFile) throws Exception {
        System.out.println("Generating RSA keys");

        KeyFactory factory = KeyFactory.getInstance("RSA");
        KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
        kpg.initialize(2048);
        KeyPair kp = kpg.generateKeyPair();
        X509EncodedKeySpec pubSpec = factory.getKeySpec(kp.getPublic(),
                X509EncodedKeySpec.class);
        PKCS8EncodedKeySpec privSpec = factory.getKeySpec(kp.getPrivate(),
                PKCS8EncodedKeySpec.class);

        FileOutputStream out = new FileOutputStream(keyFile);
        DataOutputStream dat = new DataOutputStream(out);

        byte[] enc = pubSpec.getEncoded();
        dat.writeInt(enc.length);
        dat.write(enc);

        enc = privSpec.getEncoded();
        dat.writeInt(enc.length);
        dat.write(enc);
        dat.flush();
        dat.close();

        System.out.println("RSA keys saved to " + keyFile.getPath());

        return kp;
    }


    /**
     * Load an RSA key pair from the file
     */
    public static KeyPair loadKeys(File keyFile) throws Exception {
        System.out.println("Loading RSA keys");

        FileInputStream in = new FileInputStream(keyFile);
        DataInputStream dat = new DataInputStream(in);

        int len = dat.readInt();
        byte[] enc = new byte[len];
        dat.readFully(enc);
        X509EncodedKeySpec pubSpec = new X509EncodedKeySpec(enc);

        len = dat.readInt();
        enc = new byte[len];
        dat.readFully(enc);
        PKCS8EncodedKeySpec privSpec = new PKCS8EncodedKeySpec(enc);

        dat.close();

        KeyFactory factory = KeyFactory.getInstance("RSA");
        PublicKey pub = factory.generatePublic(pubSpec);
        PrivateKey priv = factory.generatePrivate(privSpec);

        System.out.println("RSA keys loaded from " + keyFile.getPath());
        return new KeyPair(pub, priv);
    }


    /**
     * Decrypt a file
     */
    public static void decrypt(KeyPair kp, File inFile, File outFile) throws Exception {
        Cipher rsa = Cipher.getInstance("RSA/ECB/OAEPWithSHA1AndMGF1Padding");

        FileInputStream in = new FileInputStream(inFile);
        DataInputStream dat = new DataInputStream(in);

        // read RSA encrypted AES key
        int len = dat.readInt();
        byte[] buf = new byte[len];
        dat.readFully(buf);
        rsa.init(Cipher.DECRYPT_MODE, kp.getPrivate());

        SecretKeySpec key = new SecretKeySpec(rsa.doFinal(buf), "AES");

        // read the AES IV
        len = dat.readInt();
        buf = new byte[len];
        dat.readFully(buf);
        IvParameterSpec iv = new IvParameterSpec(buf);

        Cipher aes = Cipher.getInstance("AES/CBC/PKCS5Padding");
        aes.init(Cipher.DECRYPT_MODE, key, iv);

        // process the rest of the file to get the original back
        FileOutputStream out = new FileOutputStream(outFile);

        buf = new byte[1000];
        int r = -1;
        while( (r = dat.read(buf)) != -1 ) {
            byte[] sec = aes.update(buf, 0, r);
            if (sec != null) out.write(sec);
        }
        out.write(aes.doFinal());
        out.flush();
        out.close();
        in.close();
    }


    public static void encrypt(KeyPair kp, File inFile, File outFile) throws Exception {
        Cipher rsa = Cipher.getInstance("RSA/ECB/OAEPWithSHA1AndMGF1Padding");

        // create new AES key
        KeyGenerator gen = KeyGenerator.getInstance("AES");
        gen.init(256);
        SecretKey key = gen.generateKey();

        // RSA encrypt AES key
        byte[] keyEnc = key.getEncoded();
        rsa.init(Cipher.ENCRYPT_MODE, kp.getPublic());
        byte[] keySec = rsa.doFinal(keyEnc);

        // Create AES cipher
        Cipher aes = Cipher.getInstance("AES/CBC/PKCS5Padding");
        aes.init(Cipher.ENCRYPT_MODE, key);
        byte[] iv = aes.getIV();

        FileOutputStream out = new FileOutputStream(outFile);
        DataOutputStream dat = new DataOutputStream(out);

        // save encrypted AES key and IV
        dat.writeInt(keySec.length);
        dat.write(keySec);
        dat.writeInt(iv.length);
        dat.write(iv);

        // save the encrypted file
        FileInputStream in = new FileInputStream(inFile);
        byte[] buf = new byte[1000];
        int r = -1;
        while( (r = in.read(buf)) != -1 ) {
            byte[] sec = aes.update(buf, 0, r);
            if (sec != null) dat.write(sec);
        }
        dat.write(aes.doFinal());
        dat.flush();
        dat.close();
        in.close();
    }


    /**
     * @param args
     */
    public static void main(String[] args) throws Exception {
        if (args.length != 4) {
            System.out.println("Usage: java Test (encrpyt|decrypt) keyFile inFile outFile");
            return;
        }
        KeyPair kp;
        File keyFile = new File(args[0]);
        if (!keyFile.canRead()) {
            kp = genKeys(keyFile);
        } else {
            kp = loadKeys(keyFile);
        }

        boolean isEnc = args[1].equalsIgnoreCase("encrypt");
        boolean isDec = args[1].equalsIgnoreCase("decrypt");
        File inFile = new File(args[2]);
        File outFile = new File(args[3]);
        if (!(isEnc || isDec) || !inFile.canRead()) {
            System.out.println("Usage: java Test (encrpyt|decrypt) keyFile inFile outFile");
            return;
        }

        if (isEnc) {
            encrypt(kp, inFile, outFile);
        } else {
            decrypt(kp, inFile, outFile);
        }
    }
}
