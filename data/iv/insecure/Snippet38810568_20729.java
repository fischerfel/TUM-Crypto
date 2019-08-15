package testing;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.LineNumberReader;
import java.util.ArrayList;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class encryptedTest{

ArrayList<String> wordList = new ArrayList<String>();
static String IV = "AAAAAAAAAAAAAAAA";
File txt = new File("encrypted.txt");
FileOutputStream fileOutput;

public encryptedTest(){
    wordList.add("TestingTestingTesting"); //First line of encrypted text...If I add 4 characters I get an input error
    wordList.add("Test"); // Second line of encrypted text
    startEncryption();
}

public void startEncryption() {     
        try {
            String key = "This is the Key ";
            if(!txt.exists()){
                encrypt(key);
            }
            decrypt(key);
            encrypt(key);
        } catch (Exception e) {
            e.printStackTrace();
        }       
}

public void encrypt(String key) throws Exception {
    try{
        fileOutput = new FileOutputStream(txt);
    }catch(Exception e){
        e.printStackTrace();
    }
    for(int i = 0; i < wordList.size(); i++){
        byte[] data = wordList.get(i).getBytes();

        SecretKeySpec spec = new SecretKeySpec(key.getBytes(), "AES");

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, spec, new IvParameterSpec(IV.getBytes()));

        byte[] textEncrypted = cipher.doFinal(data);
        fileOutput.write(textEncrypted, 0, textEncrypted.length);
        if(!(i == wordList.size() - 1)){
        fileOutput.write(System.getProperty("line.separator").getBytes());
        }
    }
}

public void  decrypt(String key) throws Exception {
    LineNumberReader  lnr = new LineNumberReader(new FileReader(txt));
    BufferedReader br = new BufferedReader(new FileReader(txt));
    lnr.skip(Long.MAX_VALUE);

    for(int i = 0; i < lnr.getLineNumber() + 1; i++) {      
    try{
            FileInputStream fileInput = new FileInputStream(txt);
            SecretKeySpec spec = new SecretKeySpec(key.getBytes(), "AES");

            String encryptedText = br.readLine();
            byte[] bufferdReader = new byte[encryptedText.length()];
            bufferdReader = new String(encryptedText).getBytes();

            Cipher cipher;
            cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, spec, new IvParameterSpec(IV.getBytes()));

            byte[] decryptedText = cipher.doFinal(bufferdReader);

            System.out.println(new String(decryptedText));

    }catch(Exception exc){
            exc.printStackTrace();
        }
    }
    lnr.close();        
}

public static void main(String[] args){
    new encryptedTest();
}
}
