import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.security.MessageDigest;
import java.util.Scanner;

public class Execute {

    public static void main(String[] args) throws Exception {
        // TODO Auto-generated method stub
        Scanner scnr = new Scanner(System.in);
        FileInputStream fileByteStream = null;
        String username = "";
        String password = "";

        Authentication user = new Authentication();

        // Ask for user input
        System.out.println("Enter your username");
        username = scnr.nextLine();

        System.out.println("Enter your password");
        password = scnr.nextLine();

        // Convert the password string to an MD5 hash
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(password.getBytes());
        byte[] digest = md.digest();
        StringBuffer sb = new StringBuffer();
        for (byte b : digest) {
            sb.append(String.format("%02x", b & 0xff));
        }
        // Set the username and password to what the user entered
        user.setUsername(username);
        user.setPassword(sb.toString());

        // open the credentials file
        try {
            fileByteStream = new FileInputStream("src/credentials.txt");
        } catch (FileNotFoundException ex) {
            System.out.println("File not found");
        }
        Scanner inFS = new Scanner(fileByteStream);
        // inFS.useDelimiter("");

        int lineNumber = 1;
        while (inFS.hasNextLine()) {
            String fileLine = inFS.nextLine(); // declares string "fileLine" to
                                            // the next line in the file
            String[] fileLineArray = fileLine.split("; "); // turns fileLine into
                                                        // an array name
                                                        // fileLineArray
            lineNumber++; // increase line number by 1 each progression

            if (fileLineArray[0].matches(user.getUsername())) {
                System.out.println("Success");
            } else {
                System.out.println("Failed");
            }
        }
    }
}
