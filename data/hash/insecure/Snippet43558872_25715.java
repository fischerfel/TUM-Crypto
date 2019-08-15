package zooauthenticator;

import java.util.Scanner;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.io.*;
import java.io.BufferedReader;
import java.io.IOException;

class authentiClass {



    public void sysStart1(){
        System.out.println("--System Startup Sequence Now Loading--");
    }
    public void sysStart2(){
        System.out.println(". . .");
    }
    public void sysStart3(){
        System.out.println("Loaded!");
    }
    public void sysStart4(){
        System.out.println();
        System.out.println();
        System.out.println("Welcome to the Central Zoo Keeping System!");
        System.out.println("Admins and Users can log in below.");
    }




    private String original;
    private String value;
    private String value2;
    private static String passwordToHash;
    private static String generatedPassword;
    public void authentiUser() throws java.io.IOException{
        System.out.println("Enter Active Username: ");

        BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
        value = bufferRead.readLine();
        System.out.println(value);

        System.out.println("Enter Active Password: ");

        value2 = bufferRead.readLine();
        System.out.println(value2);

        String adminPass = "letmein";
        String adminPass2 = "animal doctor";
        String vetPass = "secret password";
        String vetPass2 = "grizzly1234";
        String zooPass = "alphabet soup";
        String zooPass2 = "M0nk3y business";


        if (value2.equals(zooPass) || value2.equals(zooPass2)){
            System.out.println("Logged in!");
            System.out.println("Hello, Zookeeper!\n" +
            "\n" +
            "As zookeeper, you have access to all of the animals' information and their daily "
            + "monitoring logs. This allows you to track their feeding habits, habitat "
            + "conditions, and general welfare.");
            System.out.println("Press 0 to log out");
            Scanner choice = new Scanner(System.in);
            int c = choice.nextInt();

            if (c == 0){
            System.out.println("Logged out.");
            }
        }else{

        }


        if (value2.equals(vetPass) || value2.equals(vetPass2)){
            System.out.println("Logged in!");
            System.out.println("Hello, Veterinarian!\n" +
            "\n" + "As veterinarian, you have access to all of the animals' health records. "
            + "This allows you to view each animal's medical history and current "
            + "treatments/illnesses (if any), and to maintain a vaccination log.");
            System.out.println("Press 0 to log out");
            Scanner choice = new Scanner(System.in);
            int c = choice.nextInt();

            if (c == 0){
            System.out.println("Logged out.");
            }
        }else{

        }

        if (value2.equals(adminPass) || value2.equals(adminPass2)){
        System.out.println("Logged in!");
        System.out.println("Hello, System Admin!\n" +
        "\n" +
        "As administrator, you have access to the zoo's main computer system.");  
        System.out.println("This allows you to monitor users in the system and their roles.");
        System.out.println("Press 0 to log out");
        Scanner choice = new Scanner(System.in);
        int c = choice.nextInt();

        if (c == 0){
            System.out.println("Logged out.");
        }
        }else{

        }
    }

    public static void main(String[] args) throws NoSuchAlgorithmException 
    {
        passwordToHash = "password";
        generatedPassword = null;
        try {
            // Create MessageDigest instance for MD5
            MessageDigest md = MessageDigest.getInstance("MD5");
            //Add password bytes to digest
            md.update(passwordToHash.getBytes());
            //Get the hash's bytes 
            byte[] bytes = md.digest();
            //This bytes[] has bytes in decimal format;
            //Convert it to hexadecimal format
            StringBuilder sb = new StringBuilder();
            for(int i=0; i< bytes.length ;i++)
            {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            //Get complete hashed password in hex format
            generatedPassword = sb.toString();
        } 
        catch (NoSuchAlgorithmException e) 
        {
            e.printStackTrace();
        }
        System.out.println(generatedPassword);
    }
}

class ZooAuthenticator {
    public void main (String[] args) throws NoSuchAlgorithmException, IOException{
        new authentiClass().sysStart1();
        new authentiClass().sysStart2();
        new authentiClass().sysStart3();
        new authentiClass().sysStart4();
        new authentiClass().authentiUser();
        new authentiClass().main(args);
}
}
