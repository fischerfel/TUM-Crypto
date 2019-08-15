import org.apache.commons.codec.binary.Base64;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.SecretKeySpec;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.PrintWriter;
import java.security.Key;
import java.security.SecureRandom;
import java.util.Scanner;

public class Test {

private static String publicName = null;
private static String privateName = null;

public static void main(String[] args) throws Exception {
    Scanner scanner = new Scanner(System.in);
    System.out.println("Choose an option: \n(1) Decrypt \n(2) Encrypt \n(3) Generate Keypair");
    int choice = scanner.nextInt();
    if(choice == 1) decrypt();
    else if(choice == 2) encrypt();
    else if(choice == 3) makeKeypair();
}

private static void makeKeypair() throws Exception {
    Scanner scanner = new Scanner(System.in);
    System.out.println("Enter the name of your public key: ");
    publicName = scanner.nextLine() + ".key";
    System.out.println("Enter the name of your private key: ");
    privateName = scanner.nextLine() + ".key";
    KeyMaker keyMaker = new KeyMaker(publicName, privateName);
    keyMaker.generateKeys();
}

public static void encrypt() throws Exception {
    Scanner scanner = new Scanner(System.in);
    System.out.println("Enter the text you want to encrypt: ");
    String toEncrypt = scanner.nextLine();
    System.out.println("Enter the name of the public key you want to use: ");
    publicName = scanner.nextLine() + ".key";
    Encrypter encrypter = new Encrypter(publicName);

    Key key = generateKey();
    String encryptedWithAES = encryptAES(toEncrypt, key);

    String encodedKey = java.util.Base64.getEncoder().encodeToString(key.getEncoded());
    String encryptedKey = encrypter.rsaEncrypt(encodedKey);
    String finalOutput = encryptedKey + encryptedWithAES;

    System.out.println("Enter the name of the file encrypted file which will be created: ");
    String fileName = scanner.nextLine();
    PrintWriter out = new PrintWriter(fileName + ".txt");
    out.println(finalOutput);
    out.close();

    System.out.println("DONE - saved as: " + fileName + ".txt");
    scanner.close();
}

public static void decrypt() throws Exception {
    Scanner scanner = new Scanner(System.in);
    System.out.println("Enter the name of your encrypted file: ");
    String fileName = scanner.nextLine() + ".txt";

    String givenInput = null;
    try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
        String line;
        while ((line = br.readLine()) != null) {
            givenInput = givenInput + line;
        }
    }
    assert givenInput != null;
    String encryptedKey = givenInput.substring(0,172);
    String encryptedWithAES = givenInput.replace(encryptedKey, "");

    System.out.println("Enter the name of your private key: ");
    privateName = scanner.nextLine() + ".key";
    Decrypter decrypter = new Decrypter(privateName);
    String decryptedKey = decrypter.rsaDecrypt(encryptedKey);

    byte[] decodedKey = java.util.Base64.getDecoder().decode(decryptedKey);
    Key originalKey = new SecretKeySpec(decodedKey, "AES");

    String decryptedWithAES = decryptAES(encryptedWithAES, originalKey);
    System.out.println(decryptedWithAES);
    scanner.close();
}

public static Key generateKey() throws Exception {
    KeyGenerator kg = KeyGenerator.getInstance("AES");
    SecureRandom random = new SecureRandom();
    kg.init(random);
    return kg.generateKey();
}

private static String encryptAES(String message, Key key) throws Exception {
    Cipher cipher = Cipher.getInstance("AES");
    cipher.init(Cipher.ENCRYPT_MODE,key);

    byte[] stringBytes = message.getBytes();
    byte[] raw = cipher.doFinal(stringBytes);
    return Base64.encodeBase64String(raw);
}

public static String decryptAES(String encrypted, Key key) throws Exception       {
    Cipher cipher = Cipher.getInstance("AES");
    cipher.init(Cipher.DECRYPT_MODE, key);

    byte[] raw = Base64.decodeBase64(encrypted);
    byte[] stringBytes = cipher.doFinal(raw);
    return new String(stringBytes, "UTF8");
}
}
