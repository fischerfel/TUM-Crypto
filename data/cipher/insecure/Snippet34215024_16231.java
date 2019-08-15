import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.*;

import javax.crypto.*;
import javax.crypto.spec.*;
import java.util.Scanner;
import javax.crypto.spec.SecretKeySpec;

public class PasswordManager3
{

    static String key = "SimplePasswordMg";
    static String password1 = "";
    static String password2 = "";
    static String username = "";


    public static void main(String[] args) 
             throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, 
             BadPaddingException, IOException 
    {

        Key aesKey = new SecretKeySpec(key.getBytes(), "AES");
        Cipher cipher = Cipher.getInstance("AES");

        System.out.println("Enter New to input a new password, or Retrieve to retrieve an old password:");
        Scanner scanner1 = new Scanner(System.in);
        String answer = scanner1.nextLine();

        if(answer.equalsIgnoreCase("New")) {

            System.out.println("Please enter a username: ");
            Scanner scanner2 = new Scanner(System.in);
            username = scanner2.nextLine();

            System.out.println("Please enter a password: ");
            Scanner scanner3 = new Scanner(System.in);
            password1 = scanner3.nextLine();

            System.out.println("Please enter your password again: ");
            Scanner scanner4 = new Scanner(System.in);
            password2 = scanner4.nextLine();

            if (password1.equalsIgnoreCase(password2)) {

                Files.write(Paths.get(username + ".txt"), encrypt(password1, cipher, aesKey));
                System.out.println("Your password has been stored.");
            }

            else {
                System.out.println("The passwords you entered did not match. Exiting password manager.");
            }

        }

        else if(answer.equalsIgnoreCase("Retrieve")) {

            System.out.println("Please enter the username you would like to retrieve the password for: ");
            Scanner scanner5 = new Scanner(System.in);
            username = scanner5.nextLine();
            BufferedReader in = new BufferedReader(new FileReader(username + ".txt"));
            String encryptedpass = in.readLine();
            byte[] encryptedpass2 = encryptedpass.getBytes("UTF-8");
            System.out.println(decrypt(encryptedpass2, cipher, aesKey));
        }

        else {
            System.out.println("You entered an incorrect option, program exited.");
        }

    }

     public static byte[] encrypt(String str, Cipher cipher, Key aesKey) 
             throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException 
     {

          cipher.init(Cipher.ENCRYPT_MODE, aesKey);

          byte[] encrypted = cipher.doFinal(key.getBytes("UTF-8"));

          return encrypted;
     }

    public static String decrypt(byte[] byte1, Cipher cipher, Key aesKey) 
            throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException 

    {

        cipher.init(Cipher.DECRYPT_MODE, aesKey);
        String decrypted = new String(cipher.doFinal(byte1));
        return decrypted;
    }

}
