import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.CipherOutputStream;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.ShortBufferException;
import javax.crypto.spec.SecretKeySpec;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.Scanner;

public class EncryptDecrypt {
    static Cipher cipher;
    static byte[] cipherText;
    static byte[] input;
    static byte k[]="2305ty6345663ty0".getBytes();
    static SecretKeySpec key = new SecretKeySpec(k, "AES");
    static int ctLength;
    static String filePath = "C:/inddexfolder/casie.jpg";
    static String encryptionPath = "C:/indexfolder1/encrypt.jpg";

        public static void main(String[] args) {
            EncryptDecrypt.encrypt();
            EncryptDecrypt.decrypt();
        }

        public static void encrypt() {
            try{
                input = filePath.getBytes();
                FileInputStream file = new FileInputStream(filePath);
                FileOutputStream outStream = new FileOutputStream(encryptionPath);

                cipher  = Cipher.getInstance("AES/ECB/PKCS5Padding", "SunJCE");

                cipher.init(Cipher.ENCRYPT_MODE, key);
                cipherText = new byte[cipher.getOutputSize(input.length)];
                ctLength = cipher.update(input, 0, input.length, cipherText, 0);
                ctLength+= cipher.doFinal(cipherText, ctLength);
                String encrypted = new String (cipherText);
                CipherOutputStream cos = new CipherOutputStream(outStream, cipher);
                byte[] buf = new byte[1024];
                int read;
                while((read=file.read(buf))!=-1){
                    cos.write(buf,0,read);
                }
                file.close();
                outStream.flush();
                cos.close();
            }
            catch(IOException e) {  
                e.printStackTrace();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (NoSuchPaddingException e) {
                e.printStackTrace();
            } catch (InvalidKeyException e) {
                e.printStackTrace();
            } catch (NoSuchProviderException e) {
                e.printStackTrace();
            } catch (IllegalBlockSizeException e) {
                e.printStackTrace();
            } catch (ShortBufferException e) {
                e.printStackTrace();
            } catch (BadPaddingException e) {
                e.printStackTrace();
            }
        }

        public static void decrypt() {
        try {
            FileInputStream file = new FileInputStream(encryptionPath);
            FileOutputStream outStream = new FileOutputStream("casenc1.jpg");
            byte k[]="2305ty6345663ty0".getBytes();
            SecretKeySpec key = new SecretKeySpec(k, "AES");
            cipher  = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, key);
            CipherOutputStream cos = new CipherOutputStream(outStream, cipher);
            byte[] buf = new byte[1024];
            int read;
            while((read=file.read(buf))!=-1) {
                cos.write(buf,0,read);
            }
            file.close();
            outStream.flush();
            cos.close();

         Runtime.getRuntime().exec("rundll32 url.dll, FiProtocolHandler 
          "+"casenc1.jpg");

        } catch(IOException e) {
            System.out.println(" not decrypted Successfully");
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        }
    }
}
