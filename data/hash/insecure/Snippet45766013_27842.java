package authenticationsystem;
import java.security.MessageDigest;
import java.io.*;
import java.util.*;

public class UserInfo {
private Scanner x;
private Scanner z;
private String user;
private String pass;
private String role;
private String hash;
private boolean trip = false;

public void userName(String name) throws Exception {
    Scanner scnr = new Scanner(System.in);
    String userLine, hashCode, password="", userPass, roleFile;
    int quotes, quotes2, lineLength, usernameLength, hashLength;
    int lineNumber = 0, i = 0;

    user = name;

    try{
        x = new Scanner(new File("src\\authenticationsystem\\credentials.txt"));
    }
    catch(Exception e) {
        System.out.println("could not find file");
    }

    while(x.hasNextLine()) {
        userLine = x.nextLine();
        if (userLine.contains(user)) {

            usernameLength = user.length();
            lineLength = userLine.length();
            quotes = userLine.indexOf('\"');
            quotes2 = userLine.lastIndexOf('\"');

            //password = userLine.substring((quotes + 1), quotes2);

            //System.out.println(password);
            System.out.println(usernameLength + " " + lineLength);
            System.out.println("Please enter your password");
            userPass = scnr.nextLine();

            //userPass = password;
            hashCode = userLine.substring((usernameLength + 1), (usernameLength + 32));
            roleFile = userLine.substring((quotes2 + 1), lineLength);
            setPassword(userPass);
            setHashCode(hashCode, roleFile);
            user = userLine.substring(0, usernameLength);
            }
        else if (user.equals("Exit") || user.equals("exit")) {
            System.exit(i);
        }
        lineNumber++;    
        }      
}
String getName() {
    return user;
}
public void setPassword(String passW) throws Exception {
    pass = passW;

    String original = passW;  //Replace "password" with the actual password inputted by the user
    MessageDigest md = MessageDigest.getInstance("MD5");
    md.update(original.getBytes());
    byte[] digest = md.digest();
  StringBuffer sb = new StringBuffer();
    for (byte b : digest) {
        sb.append(String.format("%02x", b & 0xff));
    }

    hash = sb.toString();        
}
public String getPassword() {
    return pass;
}
public void setHashCode(String hashC, String roleF) {
    AuthenticationSystem mane = new AuthenticationSystem();
    Scanner scnr = new Scanner(System.in);

    if (hash.contains(hashC)) {
        if (roleF.contains("admin") || roleF.contains("Admin")) {
        try{
            z = new Scanner(new File("src\\authenticationsystem\\admin.txt"));
        }
        catch(Exception e) {
            System.out.println("could not find file");
        }
        while(z.hasNext()) {
            String a = z.nextLine();
            String b = z.nextLine();
            String c = z.nextLine();

            System.out.printf("\n%s\n%s\n%s\n", a, b, c);
        }
        role = roleF;
    }
    else if (roleF.contains("veterinarian") || roleF.contains("Veterinarian")) {
        try{
            z = new Scanner(new File("src\\authenticationsystem\\veterinarian.txt"));
        }
        catch(Exception e) {
            System.out.println("could not find file");
        }
        while(z.hasNext()) {
            String a = z.nextLine();
            String b = z.nextLine();
            String c = z.nextLine();

            System.out.printf("\n%s\n%s\n%s\n", a, b, c);
        }
        role = roleF;
    }
    else if (roleF.contains("zookeeper") || roleF.contains("Zookeeper")) {
        try{
            z = new Scanner(new File("src\\authenticationsystem\\zookeeper.txt"));
        }
        catch(Exception e) {
            System.out.println("could not find file");
        }
        while(z.hasNext()) {
            String a = z.nextLine();
            String b = z.nextLine();
            String c = z.nextLine();

            System.out.printf("\n%s\n%s\n%s\n", a, b, c);
        }
        role = roleF;
    }
    }
    else {
        System.out.println("Invalid hash codes.");
    }
}
public String getHashCode() {
    return hash;
}
public void closeFile () {
    x.close();
}
