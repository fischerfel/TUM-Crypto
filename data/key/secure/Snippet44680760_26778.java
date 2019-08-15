package ReadFileExample;


import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

import java.security.KeyStore;



public class generatekey {


  static Cipher cipher;
  public static void main(String[] args) throws Exception {






    // generating a symmetric key using the AES algorithm
    KeyGenerator generator = KeyGenerator.getInstance("AES");
    // 128 bit key
    generator.init(256);
    //generates a secret key
    SecretKey secretkey = generator.generateKey();
    // returns an AES cipher
    cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
    //print key
    System.out.println("Key: " + cipher);








    String plainText = "Hello World";
    // call to method encrypt 
    String hexEncryptedByteText  = encrypt(plainText, secretkey);
    // print orignial text and encrypted text
    System.out.println("Plain Text: " + plainText);
    System.out.println("Encrypted Text: " + hexEncryptedByteText);

    int plainTextlength = plainText.length();
    System.out.println("length of text: " + plainTextlength);


    // allows to write data to a file
    FileOutputStream fos = null;
    // write bytes to file
    BufferedOutputStream bos = null;
    // create file to which data needs to be written
    String fileName = "C:/Users/******/newFile.txt";

    try{
        // allows written data to go into the written path
        fos = new FileOutputStream(fileName);
        // converts written data into bytes
        bos = new BufferedOutputStream(fos);


        // writes the encrypted text into file
        bos.write(hexEncryptedByteText.length());                


        System.out.println("encryptedText has been written successfully in "
                     +fileName);

        // allows to catch bug in code
    } catch (IOException e) {
        e.printStackTrace();
    } finally {
        try{
            // check for null exception
            if (bos != null){
                bos.close();

            }
            // check for null exception
            if (fos != null){
                fos.close();
            }
        } catch (IOException e){
            e.printStackTrace();

        }



    }






    // creates a file input stream by opening a path to the file needed
    FileInputStream fin = new FileInputStream("C:/Users/*****/public.cert");
    // implements the X509 certificate type
    CertificateFactory f = CertificateFactory.getInstance("X.509");
    // initalizes data found in the file
    X509Certificate certificate = (X509Certificate)f.generateCertificate(fin);
    // gets public key from this certificate 
    PublicKey pk = certificate.getPublicKey();
    System.out.println(pk);

    String hexEncryptedByteKey = encryptedKey(pk, secretkey);
    System.out.println("Encrypted Key: " + hexEncryptedByteKey);
    System.out.println("Encrypted Key length: " + hexEncryptedByteKey.length());

    // allows to write data to a file
    FileOutputStream newFos = null;
    // write bytes to file
    BufferedOutputStream newBos = null;
    // create file to which data needs to be written
    String fileNameKey = "C:/Users/****/symmetric.txt";

    try{
        // allows written data to go into the written path
        newFos = new FileOutputStream(fileNameKey);
        // converts written data into bytes
        newBos = new BufferedOutputStream(newFos);



        // writes the encrypted text into file
        newBos.write(hexEncryptedByteKey.length());                


        System.out.println("encryptedKey has been written successfully in "
                     +fileNameKey);






        // allows to catch bug in code
    } catch (IOException e) {
        e.printStackTrace();
    } finally {
        try{
            // check for null exception
            if (newBos != null){
                newBos.close();

            }
            // check for null exception
            if (newFos != null){
                newFos.close();
            }
        } catch (IOException e){
            e.printStackTrace();

        }
    }

    // load keystore to get private key
    KeyStore ks = KeyStore.getInstance("JKS");
    String password = "*****";
    char[] passwordChar = password.toCharArray();
    System.out.println("password: " + passwordChar);
    // locate file
    try (FileInputStream fis = new FileInputStream("C:/Users/*****/keystore.jks")) {
        ks.load(fis, passwordChar);
    }

    // protect password for keystore
    KeyStore.ProtectionParameter protParam = new KeyStore.PasswordProtection(passwordChar);

    // get private key from keystore 
    KeyStore.PrivateKeyEntry pkEntry = (KeyStore.PrivateKeyEntry)
            ks.getEntry("*****", protParam);

    PrivateKey myPrivateKey = pkEntry.getPrivateKey();

    System.out.println("private key: " + myPrivateKey);
    //method declaration
    String decryptedKey = decryptedKey(myPrivateKey, hexEncryptedByteKey);

    System.out.println("decrypted Key: " + decryptedKey);

    String hexDecryptedByteText = decryptedTextHex(decryptedKey, hexEncryptedByteText);
    System.out.println("key: " + hexDecryptedByteText);

}




public static String encrypt(String plainText, SecretKey secretkey) throws Exception {
    //Encodes the string into a sequence of bytes
    byte[] plainTextByte = plainText.getBytes();
    //intialize cipher to encryption mode
    cipher.init(Cipher.ENCRYPT_MODE, secretkey);
    //data is encrypted 
    byte[] encryptedByte = cipher.doFinal(plainTextByte);
    //Base64.Encoder encoder = Base64.getEncoder();
    //encodes bytes into a string using Base64
    byte[] encryptedByteText = Base64.getEncoder().encode(plainTextByte);
    String hexEncryptedByteText = DatatypeConverter.printHexBinary(plainTextByte);
    // return the string encrypted text to the main method
    return hexEncryptedByteText;

}

public static String encryptedKey(PublicKey pk, SecretKey secretkey) throws Exception {
    // data written to byte array
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    // writes data types to the output stream
    ObjectOutputStream writter = new ObjectOutputStream(baos);
    //specific object of secretkey is written to the output stream
    writter.writeObject(secretkey);

    //creates a byte array  
    byte[] plainTextByteKey = baos.toByteArray();

    //creates a cipher using the RSA algorithm 
    Cipher cipher = Cipher.getInstance("RSA");
    // initalizes cipher for encryption using the public key 
    cipher.init(Cipher.ENCRYPT_MODE, pk);
    //encrypts data
    //byte[] encryptedByteKey = Base64.getEncoder().encode(plainTextByteKey);

    String hexEncryptedByteKey = DatatypeConverter.printHexBinary(plainTextByteKey);
    //Base64.Encoder encoderKey = Base64.getEncoder();
    // encodes the byte array into a string.
    //String encryptedTextKey = new String(encryptedByteKey);
    return hexEncryptedByteKey;

}

private static String decryptedKey(PrivateKey myPrivateKey, String hexEncryptedByteKey) throws Exception {

    //ByteArrayOutputStream baosDecrypt = new ByteArrayOutputStream();
    //ObjectOutputStream writterDecrypt = new ObjectOutputStream(baosDecrypt);
    //writterDecrypt.writeObject(hexEncryptedByteKey);
    //byte[] byteKeyDecrypt = baosDecrypt.toByteArray();


    Cipher cipher;
    cipher = Cipher.getInstance("RSA");
    cipher.init(Cipher.DECRYPT_MODE, myPrivateKey);
    //cipher.doFinal();

    //byte [] decryptedKey = Base64.getDecoder().decode(byteKeyDecrypt);
    //String decryptedTextKey = new String(byteKeyDecrypt);

    byte[] decodedHex = DatatypeConverter.parseHexBinary(hexEncryptedByteKey);
    System.out.println("decoded hex key: " + decodedHex);
    String decryptedKey = new String(decodedHex, "UTF-8");
    return decryptedKey;





}

private static String decryptedTextHex(String decryptedKey, String hexEncryptedByteText) throws Exception {

    byte[] decryptedTextByte = decryptedKey.getBytes();
    byte[] textString = hexEncryptedByteText.getBytes();
    SecretKey key = new SecretKeySpec(decryptedTextByte, 0, decryptedTextByte.length, "AES");


    Cipher cipher;
    cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
    //IvParameterSpec iv = new IvParameterSpec(cipher.getIV());
    cipher.init(Cipher.DECRYPT_MODE, key);
    byte[] decodedTextHex = cipher.doFinal(textString);

    byte[] decoded = Base64.getDecoder().decode(decodedTextHex);

    String hexDecryptedByteText = DatatypeConverter.printHexBinary(decoded);


    return hexDecryptedByteText;



}
