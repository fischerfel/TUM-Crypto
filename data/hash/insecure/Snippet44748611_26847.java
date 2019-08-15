package authentication;

import java.io.File;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.util.List;
import java.util.Scanner;

public class Authentication {

public static void main(String args[]) {

    try {
        Scanner scan = null;
        scan = new Scanner(new File("credentials.txt"));
        String credentials[][] = new String[100][4];
        int count = 0;
        while (scan.hasNextLine()) {


            credentials[count][0] = scan.next();
            credentials[count][1] = scan.next();

            String l[] = scan.nextLine().split("\"[ ]+");
            l[0] = l[0].trim();
            l[0] = l[0].replace("\"", "");

            credentials[count][2] = l[0];
            credentials[count][3] = l[0].trim();
            count++;
        }

//Ask user what they would like to do and allow for only 3 failed login attempts
        Scanner scanio = new Scanner(System.in);
        boolean RUN = true;
        int tries = 0;

        while (RUN) {
            System.out.println("Hello.");
            System.out.println("Enter 1 to enter username and Password");
            System.out.println("Enter 2 to exit the program");
            System.out.println("*Warning. After 3 failed login attempts you will no longer be allowed to enter the program.");


            int ch = Integer.parseInt(scanio.nextLine().trim());

            if (ch == 1) {

                tries++;



//Ask User to Input their Username and then Password
                System.out.print("Input username:");
                String username = scanio.nextLine();
                System.out.print("Input password:");
                String password = scanio.nextLine();



//MD5 Hash generator
                MessageDigest md;
                md = MessageDigest.getInstance("MD5");
                md.update(password.getBytes());
                byte[] digest = md.digest();
                StringBuilder sb = new StringBuilder();
                for (byte b : digest) {
                    sb.append(String.format("%02x", b & 0xff));
                }
                String hPassword = sb.toString();

                boolean badUser = true;
                for (int i = 0; i < count; i++) {
                    if (username.contentEquals(credentials[i][0])) {
                        if (hPassword.contentEquals(credentials[i][1])) {                 

                            List<String> data = null;

//Look up which credentials are needed and then print.

                            switch (credentials[i][3]) {
                                case "zookeeper":
                                    data = Files.readAllLines(Paths.get("zookeeper.txt"), Charset.defaultCharset());
                                    break;
                                case "admin":
                                    data = Files.readAllLines(Paths.get("admin.txt"), Charset.defaultCharset());
                                    break;
                                case "veterinarian":
                                    data = Files.readAllLines(Paths.get("veterinarian.txt"), Charset.defaultCharset());
                                    break;
                                default:
                                    break;
                            }

                            for (String s : data) {
                                System.out.println(s);
                            }                         

//Give user option to log out or exit the program

                            System.out.println("\n1) Logout.");
                            System.out.println("2) Exit.");

                            ch = Integer.parseInt(scanio.nextLine().trim());
                            if (ch == 2) {
                                RUN = false;
                            }

                            badUser = false;
                            break;
                        }
                    }
                }

                if (badUser) {
                    System.out.println("Invalid Username or password.");
                }

            } else {
                RUN = false;
                break;
            }

//Maxinum number of allowed attempts have been reached
            if (tries == 3) {
                RUN = false;
                System.out.println("You have reached the maximum number of failed login attempts.");
                System.out.println("Please contact your system administrator for assistance.");
                System.out.println("Goodbye.");

            }

        }

    } catch (Exception ex) {
        ex.printStackTrace();
    }

}
