     package AuthenticationSystem;

     //import necessary java tools
     import java.io.FileInputStream;
     import java.io.BufferedReader;
     import java.io.InputStreamReader;
     import java.security.MessageDigest;
     import java.util.Scanner;
     import javax.swing.JOptionPane;

     public class Authenticationsystem {

    public static void main(String[] args) throws Exception {

    Scanner scnr = new Scanner(System.in);

    // fileinput reading txt iles
    FileInputStream fis = null;
    InputStreamReader isr = null;
    BufferedReader br = null;
    String txtline = "";

    //credentials elements
    final int NUM_ELEMENTS = 6;
    String[] storedUsername = new String[NUM_ELEMENTS];
    String[] role = new String[NUM_ELEMENTS];
    String[] hashPassword = new String[NUM_ELEMENTS];
    String username = "";
    String password = "";
    String hash = "";

    int i = 0;
    int j = 0;
    int user = 0;
    boolean verified = false;

    // user / pw array
    storedUsername[i] = "griffin.keyes";
    hashPassword[i] = "108de81c31bf9c622f76876b74e9285f";
    ++i;
    storedUsername[i] = "rosario.dawson";
    hashPassword[i] = "3e34baa4ee2ff767af8c120a496742b5";
    ++i;
    storedUsername[i] = "bernie.gorilla";
    hashPassword[i] = "a584efafa8f9ea7fe5cf18442f32b07b";
    ++i;
    storedUsername[i] = "donald.monkey";
    hashPassword[i] = "17b1b7d8a706696ed220bc414f729ad3";
    ++i;
    storedUsername[i] = "jerome.grizzlybear";
    hashPassword[i] = "3adea92111e6307f8f2aae4721e77900";
    ++i;
    storedUsername[i] = "bruce.grizzlybear";
    hashPassword[i] = "0d107d09f5bbe40cade3de5c71e9e9b7";
    ++i;

    //dialog box entry
    JOptionPane.showMessageDialog(null, "All entries are case sensitive.\n"
            + "Enter \"quit\" at any time to exit.", "Authentication System", JOptionPane.PLAIN_MESSAGE);

    // enter username / password
    username = (String) JOptionPane.showInputDialog(null, "Enter username: ", "Authentication System", JOptionPane.PLAIN_MESSAGE);
    if (username.equals("quit")) {
        JOptionPane.showMessageDialog(null, "You have chosen to exit. Goodbye.", "Authentication System", JOptionPane.WARNING_MESSAGE);
        return;
    }
    password = (String) JOptionPane.showInputDialog(null, "Enter password: ", "Authentication System", JOptionPane.PLAIN_MESSAGE);
    if (password.equals("quit")) {
        JOptionPane.showMessageDialog(null, "You have chosen to exit. Goodbye.", "Authentication System", JOptionPane.WARNING_MESSAGE);
        return;
    }

    //exit after three checks
    for (i = 0; i < 3; ++i) {

        // check user credentials 
        for (j = 0; j < NUM_ELEMENTS; ++j) {
            if (username.equals(storedUsername[j])) {
                String original = password;  // md5digest
                MessageDigest md = MessageDigest.getInstance("MD5");
                md.update(original.getBytes());
                byte[] digest = md.digest();
                StringBuilder sb = new StringBuilder();
                for (byte b : digest) {
                    sb.append(String.format("%02x", b & 0xff));
                }

                verified = true;
                user = j;
                hash = sb.toString();
            }
        }
        //if wrong entry, re-enter
        if (!hash.equals(hashPassword[user])) {
            verified = false;
            if (i < 2) {
                JOptionPane.showMessageDialog(null, "Invalid user credentials. " + (2 - i)
                        + " attempt(s) remaining.", "Authentication System", JOptionPane.PLAIN_MESSAGE);
                username = (String) JOptionPane.showInputDialog(null, "Enter username: ", "Authentication System",
                        JOptionPane.PLAIN_MESSAGE);
                if (username.equals("quit")) {
                    JOptionPane.showMessageDialog(null, "You have chosen to exit. Goodbye.", "Authentication System",
                            JOptionPane.WARNING_MESSAGE);
                    break;
                }
                password = (String) JOptionPane.showInputDialog(null, "Enter password: ", "Authentication System",
                        JOptionPane.PLAIN_MESSAGE);
                if (password.equals("quit")) {
                    JOptionPane.showMessageDialog(null, "You have chosen to exit. Goodbye.", "Authentication System",
                            JOptionPane.WARNING_MESSAGE);
                    break;
                }
            }
            //closes program if tries exceed 3
            if ((!verified) && (i == 2)) {
                JOptionPane.showMessageDialog(null, "Could not verify credentials. Goodbye.", "Authentication System",
                        JOptionPane.ERROR_MESSAGE);
                break;
            }
        } // if user credentials work
        else {

            //get input from txt files
            // user if zookeeper
            if (username.equals(storedUsername[0]) || username.equals(storedUsername[3])) {
                fis = new FileInputStream("src\\AuthenticationSystem\\zookeeper.txt");
                isr = new InputStreamReader(fis);
                br = new BufferedReader(isr);
                while ((txtline = br.readLine()) != null) {
                    System.out.println(txtline);

                }
            } //user is a veternarian
            else if (username.equals(storedUsername[2]) || username.equals(storedUsername[4])) {
                fis = new FileInputStream("src\\AuthenticationSystem\\veterinarian.txt");
                isr = new InputStreamReader(fis);
                br = new BufferedReader(isr);
                while ((txtline = br.readLine()) != null) {
                    System.out.println(txtline);
                }
            } //user is an admin
            else {
                fis = new FileInputStream("src\\AuthenticationSystem\\admin.txt");
                isr = new InputStreamReader(fis);
                br = new BufferedReader(isr);
                while ((txtline = br.readLine()) != null) {
                    System.out.println(txtline);

                }
            }
            System.out.println();
            class logout {

                public void logout(String original) throws Exception {
                    int i = 0;
                    Scanner input = new Scanner(System.in);
                    while (i == 0) {
                        System.out.println("To log out please press 1 then the enter key");
                        int result = input.nextInt();
                        if (result == 1) {
                            System.out.println("You have logged out.  Have a great day!");
                            i = 1;
                        } else {
                            System.out.println("You are still logged in.");
                        }
                    }
                }
            }

        }
    }
}
