package application;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Arrays;

import javafx.application.Platform;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.spec.SecretKeySpec;

public class AESEncryption {

private final File toBeUsed;
private final String password;

public AESEncryption(String passkey, File given){
    this.password = passkey;
    this.toBeUsed = given;
}

public void encrypt(boolean toBeLocal){
    Service<Void> encryption = new Service<Void>() {

        @Override
        protected Task<Void> createTask() {

            return new Task<Void>(){

                @Override
                protected Void call() throws Exception {
                    if(toBeLocal) this.startEncryption();
                    this.success();
                    return null;

                }

                private void success() {
                    Platform.runLater(new Runnable() {

                        @Override
                        public void run() {
                            EncryptionSuccessController controller = (EncryptionSuccessController)new OfflineWindow("/fxml/encryption_success.fxml", "Success").getLoader().getController();
                            controller.setPath(toBeUsed.getAbsolutePath()+".enc");
                        }

                    });
                }

                private void startEncryption() throws Exception{
                     SecureRandom randomizer = new SecureRandom();
                     byte[] salt = new byte[16];
                     randomizer.nextBytes(salt);

                     byte key[] = (password+salt).getBytes("UTF-8");  
                     MessageDigest sha = MessageDigest.getInstance("SHA-1");
                     key = sha.digest(key);
                     key = Arrays.copyOf(key, 16);

                     SecretKeySpec pass = new SecretKeySpec(key,"AES");  
                     Cipher encrypt =  Cipher.getInstance("AES");  
                     encrypt.init(Cipher.ENCRYPT_MODE, pass);

                     FileOutputStream fos = new FileOutputStream(toBeUsed.getAbsolutePath() +".enc");
                     try(FileInputStream fis =new FileInputStream(toBeUsed.getAbsolutePath())){
                        try(CipherOutputStream cout=new CipherOutputStream(fos, encrypt)){
                            copy(fis,cout);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                     }
                }

                private void copy(InputStream is,OutputStream os) throws Exception{
                     byte buf[] = new byte[4096];
                     int read = 0;
                     while((read = is.read(buf)) != -1) os.write(buf,0,read); 
                }
            };

        };
    };
    encryption.start(); 
}

public void decrypt(boolean isLocallyEncrypted){
    Service<Void> decryption = new Service<Void>() {

        @Override
        protected Task<Void> createTask() {

            return new Task<Void>(){

                @Override
                protected Void call() throws Exception {

                    if(isLocallyEncrypted) this.startDecryption();
                    this.message();
                    return null;

                }

                private void message() {
                    Platform.runLater(new Runnable(){

                        @Override
                        public void run() {
                            DecryptionCompleteController controller = (DecryptionCompleteController)new OfflineWindow("/fxml/decryption_over.fxml", "Completed").getLoader().getController();
                            controller.setPath(toBeUsed.getAbsolutePath());
                        }

                    });
                }

                private void startDecryption() throws Exception{
                    SecureRandom randomizer = new SecureRandom();
                    byte[] salt = new byte[16];
                    randomizer.nextBytes(salt);
                    byte key[] = (password+salt).getBytes("UTF-8");  
                    MessageDigest sha = MessageDigest.getInstance("SHA-1");
                    key = sha.digest(key);
                    key = Arrays.copyOf(key, 16); 

                    SecretKeySpec pass = new SecretKeySpec(key,"AES");  
                    Cipher decrypt =  Cipher.getInstance("AES");  
                    decrypt.init(Cipher.DECRYPT_MODE, pass);
                    FileInputStream fis = new FileInputStream(toBeUsed.getAbsolutePath());
                    try(CipherInputStream cin=new CipherInputStream(fis, decrypt)){  
                       try(FileOutputStream fos =new FileOutputStream(toBeUsed.getAbsolutePath().substring(0,toBeUsed.getAbsolutePath().lastIndexOf(".")))){
                          copy(cin,fos);
                       }
                    }
                }

                private void copy(InputStream is,OutputStream os) throws Exception{
                     byte buf[] = new byte[4096];
                     int read = 0;
                     while((read = is.read(buf)) != -1) os.write(buf,0,read); 
                }
            };

        };

    };
    decryption.start();
  }

}
