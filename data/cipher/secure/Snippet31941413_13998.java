package com;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.GeneralSecurityException;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Security;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Enumeration;
import java.util.Scanner;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

import javax.crypto.Cipher;

public class Main {

public final static int BUFFER_SIZE = 117;

public static void decrypt(String originalZipFileName, String newZipFileName, String privateKeyFileName) throws Exception {
    byte[] buffer = new byte[BUFFER_SIZE];  

    ZipFile originalZipFile = new ZipFile(originalZipFileName); 
    ZipOutputStream newZipFile = new ZipOutputStream(new FileOutputStream(newZipFileName));

    Enumeration<? extends ZipEntry> zipEntries = originalZipFile.entries();

    String privateKey = getKeyString(privateKeyFileName);
    PrivateKey key = makePrivateKey(privateKey);

    Cipher cipher = Cipher.getInstance("RSA");

    cipher.init(Cipher.DECRYPT_MODE, key);
    File file = new File("temp.txt");

    while(zipEntries.hasMoreElements()){

        ZipEntry entry = zipEntries.nextElement();          

        ZipEntry copy = new ZipEntry(entry.getName());      
        newZipFile.putNextEntry(copy);

        int read;           
        InputStream inputEntry = originalZipFile.getInputStream(entry);
        OutputStream outputFile = new FileOutputStream(file);

        while((read = inputEntry.read(buffer)) != -1){              
            outputFile.write(cipher.doFinal(buffer), 0, read);
        }

        InputStream inputTempFile = new FileInputStream(file);

        while((read = inputTempFile.read(buffer)) != -1){
            newZipFile.write(buffer, 0, read);
        }

        newZipFile.closeEntry();
        inputEntry.close();
        inputTempFile.close();
        outputFile.close();
        file.delete();
    }
    newZipFile.close();
}

public static void encrypt(String originalZipFileName, String newZipFileName, String publicKeyFileName) throws Exception{

    byte[] buffer = new byte[BUFFER_SIZE];  

    ZipFile originalZipFile = new ZipFile(originalZipFileName); 
    ZipOutputStream newZipFile = new ZipOutputStream(new FileOutputStream(newZipFileName));

    Enumeration<? extends ZipEntry> zipEntries = originalZipFile.entries();

    String publicKey = getKeyString(publicKeyFileName);
    PublicKey key = makePublicKey(publicKey);

    Cipher cipher = Cipher.getInstance("RSA");

    cipher.init(Cipher.ENCRYPT_MODE, key);
    File file = new File("temp.txt");

    while(zipEntries.hasMoreElements()){

        ZipEntry entry = zipEntries.nextElement();          

        ZipEntry copy = new ZipEntry(entry.getName());      
        newZipFile.putNextEntry(copy);

        int read;           
        InputStream inputEntry = originalZipFile.getInputStream(entry);
        OutputStream outputFile = new FileOutputStream(file);

        while((read = inputEntry.read(buffer)) != -1){              
            outputFile.write(cipher.doFinal(buffer), 0, read);
        }

        InputStream inputTempFile = new FileInputStream(file);

        while((read = inputTempFile.read(buffer)) != -1){
            newZipFile.write(buffer, 0, read);
        }

        newZipFile.closeEntry();
        inputEntry.close();
        inputTempFile.close();
        outputFile.close();
        file.delete();
    }
    newZipFile.close();
}   

public static String getKeyString(String fileName){

    String key = new String();
    try {
        BufferedReader buf = new BufferedReader(new FileReader(fileName));
        key = buf.readLine();       
    } catch ( IOException e) {
        e.printStackTrace();
    }   

    return key.trim();
}

public static PublicKey makePublicKey(String stored) throws GeneralSecurityException {
    byte[] data = Base64.getDecoder().decode(stored);
    X509EncodedKeySpec spec = new  X509EncodedKeySpec(data);
    KeyFactory fact = KeyFactory.getInstance("RSA");
    return fact.generatePublic(spec);
}

public static PrivateKey makePrivateKey(String stored) throws GeneralSecurityException {
    byte[] data = Base64.getDecoder().decode(stored);
    PKCS8EncodedKeySpec spec = new  PKCS8EncodedKeySpec(data);
    KeyFactory fact = KeyFactory.getInstance("RSA");
    return fact.generatePrivate(spec);
}

public static void main(String[] args) throws Exception {

    Scanner scan = new Scanner(System.in);

    System.out.println("Enter type of operation:");
    String line = scan.nextLine();

    if(line.equals("encrypt")){
        System.out.println("Enter name of original ZIP file:");
        String originalZipFileName = scan.nextLine();

        System.out.println("Enter name of new ZIP file:");
        String newZipFileName = scan.nextLine();

        System.out.println("Enter name of file containg public key:");
        String publicKeyFileName = scan.nextLine();

        encrypt(originalZipFileName, newZipFileName, publicKeyFileName);        
    }
    if(line.equals("decrypt")){
        System.out.println("Enter name of original ZIP file:");
        String originalZipFileName = scan.nextLine();

        System.out.println("Enter name of new ZIP file:");
        String newZipFileName = scan.nextLine();

        System.out.println("Enter name of file containg private key:");
        String privateKeyFileName = scan.nextLine();

        decrypt(originalZipFileName, newZipFileName, privateKeyFileName);       
    }       

}

}
