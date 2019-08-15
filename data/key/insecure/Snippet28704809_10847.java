import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Security;
import java.util.Arrays;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.ShortBufferException;
import javax.crypto.spec.SecretKeySpec;



@SuppressWarnings("unused")
public class AESCTR {
    static String inputFile = "plain-in.txt";
    static String outputFile = "cipher-out.txt";
    static String cInputFile = "cipher-in.txt";
    static String cOutputFile = "plain-out.txt";
    static String mode;
    static String IV;
    static String key;
    public static void main(String[] args) {
        if (Security.getProvider("BC") == null){
            System.out.println("Bouncy Castle provider is NOT available");
            System.exit(-1);
        }
        else{
            System.out.println("Bouncy Castle provider is available");
        } 
        Security.addProvider(Security.getProvider("BC")); 
        // TODO Auto-generated method stub
        if (args.length != 3){
            System.out.println("Invalid number of arguments\n");
            System.exit(-1);
        }
        mode = args[0];
        IV = args[1];
        key = args[2];
        if ((!mode.equals("enc")) && (!mode.equals("dec"))){
            System.out.println("Invalid mode\n");
            System.exit(-1);
        }
        byte[] keyBytes = new byte[] { 0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 
    0x08, 0x09, 0x0a, 0x0b, 0x0c, 0x0d, 0x0e, 0x0f};
        System.out.println(keyBytes.length);
        SecretKeySpec key = new SecretKeySpec(keyBytes, "AES");
        System.out.println("Mode: " + mode + " IV: " + IV + " Key: " + key);
        if(mode.equals("enc")){
            int ctLength = 0;
            byte[] data = null;
            try {
                Path path = Paths.get(inputFile);
                data = Files.readAllBytes(path);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                System.out.println("Invalid Path\n");
                System.exit(-1);
             }
            while((data.length % 16) != 0){ //Padding
                byte[] dest = new byte[data.length + 1];
                byte[] pad = new byte[] {0x00};
                System.arraycopy(data, 0, dest, 0, data.length);
                System.arraycopy(pad, 0, dest, data.length, pad.length);    
                data = dest;
            }
            System.out.println(data.length);
            byte[] cipherText = new byte[data.length];

            Cipher cipher = null;
            try {
                cipher = Cipher.getInstance("AES/ECB/NoPadding", "BC");
            } catch (NoSuchAlgorithmException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (NoSuchProviderException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (NoSuchPaddingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            System.out.println("input text : " + new String(data));

            try {
                cipher.init(Cipher.ENCRYPT_MODE, key);
            } catch (InvalidKeyException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            try {
                ctLength = cipher.update(data, 0, data.length, cipherText, 0);    
            } catch (ShortBufferException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            try {
                ctLength += cipher.doFinal(cipherText, ctLength);
            } catch (IllegalBlockSizeException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (ShortBufferException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (BadPaddingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            Writer writer = null;
            try{ writer = new BufferedWriter(new OutputStreamWriter(new     FileOutputStream(cInputFile), "utf-8"));
            writer.write(new String(cipherText));
            } catch (IOException ex){
                System.out.println("File Write Error\n");
                System.exit(-1);
            } finally{
                try{writer.close();}catch (Exception ex){}
            }

            byte[] c = null;
            try {
                Path path = Paths.get(cInputFile);
                c = Files.readAllBytes(path);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                System.out.println("Invalid Path\n");
                System.exit(-1);
            }
            System.out.println("cipher text: " + new String(c) + " bytes: " + ctLength);
        }
        else if (mode.equals("dec")){
            byte[] c = null;
            try {
                Path path = Paths.get(cInputFile);
                c = Files.readAllBytes(path);
                File f = new File(cInputFile);
                System.out.println("In Bytes: " + c.length);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                System.out.println("Invalid Path\n");
                System.exit(-1);
            }
            Cipher cipher = null;
            try {
                cipher = Cipher.getInstance("AES/ECB/NoPadding", "BC");
            } catch (NoSuchAlgorithmException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            } catch (NoSuchProviderException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            } catch (NoSuchPaddingException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
            byte[] plainText = new byte[c.length];
            try {
                cipher.init(Cipher.DECRYPT_MODE, key);
            } catch (InvalidKeyException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            int ptLength = 0;
            try {
                ptLength = cipher.update(c, 0, c.length, plainText, 0);
                System.out.println(ptLength);
            } catch (ShortBufferException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            try {
                ptLength += cipher.doFinal(plainText, ptLength);
            } catch (IllegalBlockSizeException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (ShortBufferException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (BadPaddingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            System.out.println("plain text : " + new String(plainText) + " bytes: " + ptLength);
        }
    }
}
