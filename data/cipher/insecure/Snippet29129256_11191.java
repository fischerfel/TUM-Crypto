import java.security.*;
import java.util.Scanner;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;

public class MainApp
{
    static Scanner sc = new Scanner(System.in);
    public KeyGenerator keygen;
    public SecretKey secKey;
    Cipher c;

    static SecureRandom rnd = new SecureRandom();
    static IvParameterSpec iv = new IvParameterSpec(rnd.generateSeed(8));

    public static void main(String[] args) throws Exception
    {
        MainApp theApp = new MainApp();
        theApp.start();
    }

    public void start() throws Exception
    {
        keygen = KeyGenerator.getInstance("DES");
        secKey = keygen.generateKey();

        System.out.println(secKey);

        boolean success = false;
        boolean success2 = false;
        boolean exit = false;
        int type = 0;

        do
        {
            do 
            {
                System.out.println("Weclome to the DES Encryption/Decription zone!");
                System.out.println("What form of mode do you wish to use? [E]CB or [C]BC? Type [Q]uit to exit");
                String input = sc.nextLine();

                    if(input.equalsIgnoreCase("e")){

                        type = 1;

                        do{
                            System.out.println("Do you wish to use padding? [Y]es or [N]o?");
                            input = sc.nextLine();

                            if(input.equalsIgnoreCase("y")){
                                c = Cipher.getInstance("DES/ECB/PKCS5Padding");
                                success = true;
                                success2 = true;
                            }
                            else if(input.equalsIgnoreCase("n")){
                                c = Cipher.getInstance("DES/ECB/NoPadding");
                                success = true;
                                success2 = true;
                            }
                            else{
                                System.out.println("Error - please enter a valid input");
                                success = false;
                                success2 = false;
                            }
                        }while(!success2);

                    }
                    else if(input.equalsIgnoreCase("c")){

                        type = 2;

                        do{
                            System.out.println("Do you wish to use padding? [Y]es or [N]o?");
                            input = sc.nextLine();

                            if(input.equalsIgnoreCase("y")){
                                c = Cipher.getInstance("DES/CBC/PKCS5Padding");
                                success = true;
                                success2 = true;
                            }
                            else if(input.equalsIgnoreCase("n")){
                                c = Cipher.getInstance("DES/CBC/NoPadding");
                                success = true;
                                success2 = true;
                            }
                            else{
                                System.out.println("Error - please enter a valid input");
                                success = false;
                                success2 = false;
                            }
                        }while(!success2);
                    }

                    else if(input.equalsIgnoreCase("q")){
                        System.out.println("Thanks for using me!");
                        System.exit(0);
                        success = true;
                        exit = true;
                    }
                    else{
                        System.out.println("Error - please enter a valid input");
                        success = false;
                    }
            }while(!success);


            System.out.println("Input what you wish to encrypt");
            String input = sc.nextLine();

            byte[] text = input.getBytes();

            System.out.println(type);

            System.out.println("--------------------------------------------");

            System.out.println("Text : " + new String(text));

            byte[] textEncrypted = encrypt(text, c, type);

            System.out.println("Text Encrypted : " + textEncrypted);

            byte[] textDecrypted = decrypt(textEncrypted, c, type);

            System.out.println("Text Decrypted : " + new String(textDecrypted));

            System.out.println("--------------------------------------------");

        }while(!exit);
    }

    public byte[] encrypt(byte[] b, Cipher c, int type) throws Exception
    {
        if(type == 1)
        {
        c.init(Cipher.ENCRYPT_MODE, secKey);
        }
        else if(type == 2)
        {   
            c.init(Cipher.ENCRYPT_MODE, secKey, iv);
        }
        byte[] encryptedText = null;
        try {
            encryptedText = c.doFinal(b);
        } catch (IllegalBlockSizeException e) {
            System.out.println("ERROR - If you have selected to not automatically pad your plaintext it must be a mutiple of eight bytes to be accepted. Exiting program");
            System.exit(0);
        } 

        return encryptedText;
    }

    public byte[] decrypt(byte[] b, Cipher c, int type) throws Exception
    {
        if(type == 1)
        {
        c.init(Cipher.DECRYPT_MODE, secKey);
        }
        else if(type == 2)
        {   
            c.init(Cipher.DECRYPT_MODE, secKey, iv);
        }

        byte[] decryptedText = c.doFinal(b);

        return decryptedText;

    }


}
