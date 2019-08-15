import java.io.FileNotFoundException;
import java.io.*;
import java.util.Scanner;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.security.AlgorithmParameters;
import java.security.SecureRandom;
import java.security.spec.KeySpec;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

public class AESFileEncryption {

/*public AESFileEncryption(String nameoffile){

}
public String FileReturn(String filename){
    String fl = filename;       
    return fl;      
}*/

public static void main(String[] args) throws Exception {

    File f = new File("plainfile.txt");
    File g = new File("plainfile.txt.8102");
    File fl = new File("plainfile.txt.8102");

    if(g.exists() && !g.isDirectory()){
        System.out.println("The file is already encrypted...");
        String fname = fl.getAbsolutePath();
        System.out.print("Absolute Encrypted File Pathname => "+ fname);
        System.exit(0);
    }       
    else if(f.exists() && !f.isDirectory()) { 
         System.out.println(" The file is found.The encryption process is going to begin...");

    }       
    else{
         System.out.println(" The file is missing!!!!");
         System.exit(0);
    }

    // file to be encrypted
    FileInputStream inFile = new FileInputStream("plainfile.txt");       

    // encrypted file
    FileOutputStream outFile = new FileOutputStream("plainfile.txt.8102");


    // password to encrypt the file
    Scanner scan= new Scanner(System.in);
    System.out.println("Enter the password : => ");
    String password= scan.nextLine();

    //String password = "javapapers";

    // password, iv and salt should be transferred to the other end
    // in a secure manner

    // salt is used for encoding
    // writing it to a file
    // salt should be transferred to the recipient securely
    // for decryption
    byte[] salt = new byte[8];
    SecureRandom secureRandom = new SecureRandom();
    secureRandom.nextBytes(salt);
    FileOutputStream saltOutFile = new FileOutputStream("salt.enc");
    saltOutFile.write(salt);
    saltOutFile.close();

    SecretKeyFactory factory = SecretKeyFactory
            .getInstance("PBKDF2WithHmacSHA1");
    KeySpec keySpec = new PBEKeySpec(password.toCharArray(), salt, 65536,
            256);
    SecretKey secretKey = factory.generateSecret(keySpec);
    SecretKey secret = new SecretKeySpec(secretKey.getEncoded(), "AES");

    //
    Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
    cipher.init(Cipher.ENCRYPT_MODE, secret);
    AlgorithmParameters params = cipher.getParameters();

    // iv adds randomness to the text and just makes the mechanism more
    // secure
    // used while initializing the cipher
    // file to store the iv
    FileOutputStream ivOutFile = new FileOutputStream("iv.enc");
    byte[] iv = params.getParameterSpec(IvParameterSpec.class).getIV();
    ivOutFile.write(iv);
    ivOutFile.close();

    //file encryption
    byte[] input = new byte[64];
    int bytesRead;

    while ((bytesRead = inFile.read(input)) != -1) {
        byte[] output = cipher.update(input, 0, bytesRead);
        if (output != null)
            outFile.write(output);
    }

    byte[] output = cipher.doFinal();
    if (output != null)
        outFile.write(output);

    inFile.close();
    outFile.flush();
    outFile.close();

    System.out.println("File Encrypted.");

    }

}
