import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Enumeration;
import java.util.Scanner;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

import javax.crypto.Cipher;
import javax.crypto.CipherOutputStream;

public class Main {

public final static int BUFFER_SIZE = 1024;

public static String getPublicKeyString(String fileName){

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
    byte[] data = Base64.getDecoder().decode(stored);//Base64.decode(keyBytes, Base64.DEFAULT);
    X509EncodedKeySpec spec = new  X509EncodedKeySpec(data);
    KeyFactory fact = KeyFactory.getInstance("RSA");
    return fact.generatePublic(spec);
}

public static void main(String[] args) throws Exception {

    Scanner scan = new Scanner(System.in);

    System.out.println("Enter type of operation:");
    String line = scan.nextLine();

    if(line.equals("encrypt")){
        // Reading from console
        System.out.println("Enter name of original ZIP file:");
        String originalZipFileName = scan.nextLine();

        System.out.println("Enter name of new ZIP file:");
        String newZipFileName = scan.nextLine();

        System.out.println("Enter name of file containg public key:");
        String publicKeyFileName = scan.nextLine();

        String publicKey = getPublicKeyString(publicKeyFileName);

        // Declaration

        ZipFile originalZipFile = new ZipFile(originalZipFileName);

        byte[] buffer = new byte[BUFFER_SIZE];

        ZipOutputStream newZipFile = new ZipOutputStream(new FileOutputStream(newZipFileName));

        Enumeration<? extends ZipEntry> zipEntries = originalZipFile.entries();

        //

        PublicKey key = makePublicKey(publicKey);
        //System.out.println(key.toString());
        //

        Cipher cipher = Cipher.getInstance("RSA");

        cipher.init(Cipher.ENCRYPT_MODE, key);

        while(zipEntries.hasMoreElements()){

            ZipEntry entry = zipEntries.nextElement();
            ZipEntry copy = new ZipEntry(entry);

            newZipFile.putNextEntry(copy);

            int read;

            InputStream input = originalZipFile.getInputStream(entry);

            CipherOutputStream cos = new CipherOutputStream(newZipFile, cipher);

            while((read = input.read(buffer)) != -1){
                cos.write(buffer, 0, read);
            }
            input.close();
            cos.close();
            newZipFile.closeEntry();

        }


    }
  }
}
