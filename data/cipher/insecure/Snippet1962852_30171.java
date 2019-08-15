import java.io.*;
import java.security.*;
import java.util.ArrayList;
import javax.crypto.*;

public class Checker {
    private ArrayList<String> usersList = new ArrayList<String>();
    private ArrayList<String> passwordList = new ArrayList<String>();
    private Cipher cipher = null;
    private KeyGenerator keyGen = null;
    private Key key = null;
    private PrintStream output = System.out;
    private FileOutputStream fos = null;
    Checker() {
        try {
            cipher = Cipher.getInstance("AES");
            keyGen = KeyGenerator.getInstance("AES");
            key = keyGen.generateKey();
            output = new PrintStream(new FileOutputStream("data.txt"), true);
            fos = new FileOutputStream(new File("data.txt"));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void check() {
        try {
            CipherInputStream cipherIn = new CipherInputStream(new FileInputStream(new File("data.txt")), cipher);
            cipher.init(Cipher.DECRYPT_MODE, key);

            int i; 
            while((i = cipherIn.read()) != -1){
                fos.write(i);
            }
            output.close();
        } catch (FileNotFoundException e) {
            System.err.println("filepath not found!");
        } catch (IOException e) {
            System.err.println("IOException: " + e);
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }

    }

    public void add(String user, String password) {
        if ( !(usersList.contains(user) || passwordList.contains(password))) {
            if(usersList.isEmpty() || passwordList.isEmpty()) {
                usersList.clear();
                passwordList.clear();
                usersList.add(user);
                passwordList.add(password);
            } else {
                usersList.add(usersList.size(), user);
                passwordList.add(usersList.size() - 1, password);
            }
        }
    }

    public void display() {
        System.out.println(usersList);
        System.out.println(passwordList);
    }

    public void save() {
        try {
            for (int x = 0; x < usersList.size(); x++) {
                output.print(usersList.get(x));
                output.print("|");
                output.println(passwordList.get(x));
            }
            CipherInputStream cipherIn = new CipherInputStream(new FileInputStream(new File("data.txt")), cipher);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            int i; 
            while ((i = cipherIn.read()) != -1) {
                fos.write(i);
            }

            output.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
    }
}

public class CheckerTest {
    public static void main(String[] args) {
        Checker checker = new Checker();
        checker.add("peter", "12345");
        checker.add("mike", "67890");
        checker.display();
        checker.save();
        checker.check();
    }
}
