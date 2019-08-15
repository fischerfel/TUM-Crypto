import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

public class AES {

int mul = 0;
long begin, end;

SecretKey myAesKey;
Cipher aesCipher;

/**
 * @param args
 */
public static void main(String[] args) {
    AES demo = new AES();
}

public AES() {
    // File file = new File("plainText.txt");
    // FileReader fr = null;
    BufferedReader br = null;
    // try {
    String currentLine;
    // //fr = new FileReader();
    // } catch (FileNotFoundException e1) {
    // // TODO Auto-generated catch block
    // e1.printStackTrace();
    // }

    byte[] key = "01234567".getBytes();
    byte[] plain = "A message".getBytes();
    File file = new File("plainText.txt");
    FileReader fr = null;
    try {
        fr = new FileReader(file);
    } catch (FileNotFoundException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    }
    br = new BufferedReader(fr);

    String cipher_text;

    long totaltime = 0;
    long begin;
    int i = 0;


    // do operation 1000 times...
    for (i = 0; i < 1000; i++) {
        begin = System.currentTimeMillis();
        try {
            initiateAES();
            cipher_text = Encrypt(aesCipher, br.readLine(),
                 myAesKey).toString();
            System.out.println(cipher_text);
            currentLine = Decrypt(aesCipher, cipher_text,
                 myAesKey).toString();
            System.out.println(new String(currentLine, "UTF-8"));

        } catch (Exception ex) {
            System.out.println(ex);
        }
        // do operation that you want to time in here.....

        totaltime += System.currentTimeMillis() - begin;

    }
    // at end take average nano seconds.
    System.out.println("time:" + totaltime / i);

}

private void initiateAES() {
    try {

        // create a random 128 bit key
        KeyGenerator keygenerator = KeyGenerator.getInstance("AES");
        keygenerator.init(128);
        myAesKey = keygenerator.generateKey();

        // Create the cipher
        aesCipher = Cipher.getInstance("AES/CBC/PKCS5Padding");

    } catch (Exception ex) {

    }
}

private byte[] Encrypt(Cipher cipher, String b, SecretKey key)
        throws Exception {

    cipher.init(Cipher.ENCRYPT_MODE, key);

    return cipher.doFinal();

}

private byte[] Decrypt(Cipher cipher, String r, SecretKey key)
        throws Exception {

    cipher.init(Cipher.DECRYPT_MODE, key,
            new IvParameterSpec(cipher.getIV()));

    return cipher.doFinal();

}
