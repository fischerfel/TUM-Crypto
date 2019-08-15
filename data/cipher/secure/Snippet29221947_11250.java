package test;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Security;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Scanner;

import javax.crypto.Cipher;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemWriter;

public class PublicKeyTest {

    public static final int KEY_SIZE = 1024;


    public static void main(String[] args) {
        System.out.println("0. PK Encrypt File");
        System.out.println("1. PK Decrypt File");
        System.out.println("2. PK Generate Keys");;
        Scanner scan = new Scanner(System.in);
        String choicetext = scan.nextLine();
        int choice = Integer.parseInt(choicetext);
        if (choice == 0) {
            System.out.print("Infile: ");
            String inpath = scan.nextLine();
            System.out.print("Outfile: ");
            String outpath = scan.nextLine();
            byte[] bytes = FiletoBytes(inpath);
            PublicKey publicKey = readPublicKeyNative("./public.pem");
            byte[] encryptedBytes = encrypt(bytes, publicKey);
            BytestoFile(encryptedBytes, outpath);

        } else if (choice == 1) {
            System.out.print("Infile: ");
            String inpath = scan.nextLine();
            System.out.print("Outfile: ");
            String outpath = scan.nextLine();
            byte[] bytes = FiletoBytes(inpath);
            PrivateKey privateKey = readPrivateKeyNative("./private.pem");
            byte[] decryptedBytes = decrypt(bytes, privateKey);
            BytestoFile(decryptedBytes, outpath);

        } else if (choice == 2) {
            System.out.print("Public: ");
            String publicPath = scan.nextLine();
            System.out.print("Private: ");
            String privatePath = scan.nextLine();

            writeKeys(publicPath, privatePath);
        }
        scan.close();
    }

    public static byte[] FiletoBytes(String path) {
        byte[] bytes = null;
        RandomAccessFile raf;
        try {
            raf = new RandomAccessFile(path, "rw");

            int fileLength = (int) raf.length();
            bytes = new byte[fileLength];
            raf.read(bytes);
            raf.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return bytes;
    }

    public static void BytestoFile(byte[] bytes, String path) {

        RandomAccessFile raf;
        try {
            raf = new RandomAccessFile(path, "rw");

            raf.write(bytes);
            raf.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public static PublicKey readPublicKeyNative(String publicKeyPath) {
        Security.addProvider(new BouncyCastleProvider());
        KeyFactory factory = null;
        PublicKey key = null;
        byte[] publicKeyFileBytes = null;

        try {
            publicKeyFileBytes = Files.readAllBytes(Paths.get(publicKeyPath));
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        String KeyString = new String(publicKeyFileBytes);
        //System.out.println(KeyString);
        //System.out.println("FORMATTED:");
        KeyString = KeyString.replaceAll("-----BEGIN RSA PUBLIC KEY-----", "");
        KeyString = KeyString.replaceAll("-----END RSA PUBLIC KEY-----", "");
        KeyString = KeyString.replaceAll("[\n\r]", "");
        KeyString = KeyString.trim();
        //System.out.println(KeyString);

        byte[] encoded = Base64.getDecoder().decode(KeyString);

        // PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(encoded);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(encoded);
        try {
            factory = KeyFactory.getInstance("RSA");
            key = factory.generatePublic(keySpec);
        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return key;
    }

    public static PrivateKey readPrivateKeyNative(String privateKeyPath) {
        Security.addProvider(new BouncyCastleProvider());
        KeyFactory factory = null;
        PrivateKey key = null;
        byte[] privateKeyFileBytes = null;

        try {
            privateKeyFileBytes = Files.readAllBytes(Paths.get(privateKeyPath));
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        String KeyString = new String(privateKeyFileBytes);
        //System.out.println(KeyString);
        //System.out.println("FORMATTED:");
        KeyString = KeyString.replaceAll("-----BEGIN RSA PRIVATE KEY-----", "");
        KeyString = KeyString.replaceAll("-----END RSA PRIVATE KEY-----", "");
        KeyString = KeyString.replaceAll("[\n\r]", "");
        KeyString = KeyString.trim();
        //System.out.println(KeyString);

        byte[] encoded = Base64.getDecoder().decode(KeyString);

        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(encoded);
        // X509EncodedKeySpec keySpec = new X509EncodedKeySpec(encoded);
        try {
            factory = KeyFactory.getInstance("RSA");
            key = factory.generatePrivate(keySpec);
        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return key;
    }

    public static byte[] encrypt(byte[] bytes, PublicKey key) {
        byte[] cipherText = null;
        try {
            // get an RSA cipher object and print the provider
            final Cipher cipher = Cipher.getInstance("RSA");
            // encrypt the plain text using the public key
            cipher.init(Cipher.ENCRYPT_MODE, key);
            cipherText = cipher.doFinal(bytes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cipherText;
    }

    public static byte[] decrypt(byte[] text, PrivateKey key) {
        byte[] decryptedText = null;
        try {
            // get an RSA cipher object and print the provider
            final Cipher cipher = Cipher.getInstance("RSA");

            // decrypt the text using the private key
            cipher.init(Cipher.DECRYPT_MODE, key);
            decryptedText = cipher.doFinal(text);

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return decryptedText;
    }

    public static void writeKeys(String publicPath,String privatePath){
        Security.addProvider(new BouncyCastleProvider());

        KeyPair keyPair = generateRSAKeyPair();
        RSAPrivateKey priv = (RSAPrivateKey) keyPair.getPrivate();
        RSAPublicKey pub = (RSAPublicKey) keyPair.getPublic();
        writePemFile(priv, "RSA PRIVATE KEY", privatePath);
        writePemFile(pub, "RSA PUBLIC KEY", publicPath);
    }

    private static KeyPair generateRSAKeyPair(){
        KeyPairGenerator generator=null;
        try {
            generator = KeyPairGenerator.getInstance("RSA", "BC");
        } catch (NoSuchAlgorithmException | NoSuchProviderException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        SecureRandom random = new SecureRandom();
        generator.initialize(KEY_SIZE, random);
        KeyPair keyPair = generator.generateKeyPair();

        return keyPair;
    }

    public static void writePemFile(Key key, String description,String filename) {

        PemObject pemObject = new PemObject(description, key.getEncoded());
        PemWriter pemWriter=null;
        try {
            pemWriter = new PemWriter(new OutputStreamWriter(
                    new FileOutputStream(filename)));
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        try {
            pemWriter.writeObject(pemObject);
            pemWriter.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } 

    }
}
