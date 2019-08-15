package authentication;

import java.security.MessageDigest;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Scanner;

public class ValidateCredentials {

private boolean isValid;
private String filePath;
private String credentialsFileName;

public ValidateCredentials() {
    isValid = false;
    //filePath = "C:\\Users\\joep\\Documents\\NetBeansProjects\\ Authentication\\";
    filePath = "C:\\Users\\adrian\\Desktop\\adrian\\IT-145 Foundations In App Development\\Net Beans Projects\\Authentication\\Authentication\\src\\authentication";
    credentialsFileName = "credentials";
}

public boolean isCredentialsValid(String userName, String passWord) throws Exception {
    String original = passWord;
    MessageDigest md = MessageDigest.getInstance("MD5");
    md.update(original.getBytes());
    byte[] digest = md.digest();
    StringBuffer sb = new StringBuffer();
    for (byte b : digest) {
        sb.append(String.format("%02x", b & 0xff));
    }

    System.out.println("");
    System.out.println("original:" + original);
    System.out.println("digested:" + sb.toString()); //sb.toString() is what you'll need to compare password strings

    isValid = readDataFiles(userName, sb.toString());

    return isValid;
}

public boolean readDataFiles(String userName, String passWord) throws IOException {
    FileInputStream fileByteStream1 = null; // File input stream
    FileInputStream fileByteStream2 = null; // File input stream

    Scanner inFS1 = null;                   // Scanner object
    Scanner inFS2 = null;                   // Scanner object

    String textLine = null;
    boolean foundCredentials = false;

    // Try to open file
    System.out.println("");
    System.out.println("Opening file " + credentialsFileName + ".txt");
    fileByteStream1 = new FileInputStream(filePath + "credentials.txt");
    inFS1 = new Scanner(fileByteStream1);

    System.out.println("");
    System.out.println("Reading lines of text.");

    while (inFS1.hasNextLine()) {
        textLine = inFS1.nextLine();
        System.out.println(textLine);

        if (textLine.contains(userName) && textLine.contains(passWord)) {
            foundCredentials = true;
            break;
        }
    }

    // Done with file, so try to close it
    System.out.println("");
    System.out.println("Closing file " + credentialsFileName + ".txt");

    if (textLine != null) {
        fileByteStream1.close(); // close() may throw IOException if fails
    }

    if (foundCredentials == true) {
        // Try to open file
        System.out.println("");
        System.out.println("Opening file " + userName + ".txt");
        fileByteStream2 = new FileInputStream(filePath + userName + ".txt");
        inFS2 = new Scanner(fileByteStream2);

        System.out.println("");

         while (inFS2.hasNextLine()) {
            textLine = inFS2.nextLine();
            System.out.println(textLine);
        }

        // Done with file, so try to close it
        System.out.println("");
        System.out.println("Closing file " + userName + ".txt");

        if (textLine != null) {
            fileByteStream2.close(); // close() may throw IOException if fails
        }
    }

    return foundCredentials;
}
