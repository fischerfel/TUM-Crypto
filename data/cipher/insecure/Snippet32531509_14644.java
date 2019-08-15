import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;


public class FileManagment {

    static CreateAccountFrame caf = new CreateAccountFrame();

    File file = new File("accounts.lockedfilecausewhynot");

    Cipher desCipher;

    byte[] usernameEncrypted;
    byte[] passwordEncrypted;
    byte[] chosenUsername;
    byte[] chosenPassword;
    byte[] storedUsername;
    byte[] storedPassword;
    byte[] decryptedUsername;
    byte[] decryptedPassword;

    public void createAccountsFile(){
        try {
            if(!file.exists()){
                file.createNewFile();
            }
        } catch (IOException e){
                e.printStackTrace();
        }
    }

    public void addEncryptedAccount(){
        try {
            KeyGenerator kg = KeyGenerator.getInstance("DES");
            SecretKey desKey = kg.generateKey();

            desCipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
            desCipher.init(Cipher.ENCRYPT_MODE, desKey);

            chosenUsername = caf.chooseUsername.getText().getBytes("UTF-8");
            chosenPassword = caf.choosePassword.getText().getBytes("UTF-8");
            usernameEncrypted = desCipher.doFinal(chosenUsername);
            passwordEncrypted = desCipher.doFinal(chosenPassword);

            String encryptedUsername = new String(usernameEncrypted);
            String encryptedPassword = new String(passwordEncrypted);

            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw= new BufferedWriter(fw);
            bw.write(encryptedUsername);
            bw.newLine();
            bw.write(encryptedPassword);
            bw.close(); 
        } catch (IOException | NoSuchAlgorithmException |InvalidKeyException 
            | NoSuchPaddingException | IllegalBlockSizeException | BadPaddingException e){
            e.printStackTrace();
    }
}

    public void readEcryptedText(){
        try {
            KeyGenerator kg = KeyGenerator.getInstance("DES");
            SecretKey desKey = kg.generateKey();

            FileReader fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);

            String encryptedText = br.readLine();
            System.out.println(encryptedText);

            storedUsername = encryptedText.getBytes("UTF-8");

            desCipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
            desCipher.init(Cipher.DECRYPT_MODE, desKey);

            decryptedUsername = desCipher.doFinal(storedUsername);

            String decryptedText = new String(decryptedUsername).toString();
            System.out.println("decryptedText");

        } catch (NoSuchAlgorithmException | NoSuchPaddingException 
            | InvalidKeyException | IOException | IllegalBlockSizeException | BadPaddingException e) {
        e.printStackTrace();
    }
}
