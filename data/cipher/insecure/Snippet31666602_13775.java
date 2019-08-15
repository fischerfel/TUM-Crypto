import java.io.*;
import java.util.*;
import java.nio.file.*;
import java.nio.charset.*;
import javax.crypto.*;
import javax.crypto.spec.*;
import java.security.*;

public class encrypt {
    public static void encrypt() {
        try {
            String path = null;
            String fileName;
            KeyGenerator keyGenerator;
            SecretKey secretKey = null;
            Cipher DESCipher = null;
            byte[] data;
            String encryptedData;
            File file = null, dataFile = null, keyFile = null;
            Scanner scanner;
            BufferedWriter bufferedWriter;

            path = Paths.get("").toAbsolutePath().toString();
            keyGenerator = KeyGenerator.getInstance("DES");
            secretKey = keyGenerator.generateKey();
            DESCipher = Cipher.getInstance("DES");

            if(null == secretKey || null == DESCipher) {
                System.exit(1);
            }

            scanner = new Scanner(System.in);

            System.out.print("File to encrypt: ");
            fileName = scanner.nextLine();
            file = new File(fileName);

            while(!(file.exists() && !file.isDirectory())) {
                System.out.println("Error. File was not found.\n");
                System.out.print("File to encrypt: ");

                fileName = scanner.nextLine();
                file = new File(fileName);
            }

            data = Files.readAllBytes(Paths.get(fileName));

            DESCipher.init(Cipher.ENCRYPT_MODE, secretKey);
            encryptedData = new String(DESCipher.doFinal(data));
            encryptedData = Base64.getEncoder().encodeToString(
                encryptedData.getBytes(Charset.forName("UTF-8")));

            dataFile = new File(path + "/" + fileName + ".data");
            keyFile = new File(path + "/" + fileName + ".key");

            /* Write content to file */
        } catch (Exception e) {
            /* Throw error */
            System.exit(1);
        }
    }

    public static void decrypt() {
        try {
            String path = null;
            String dataFileName, keyFileName;
            Scanner scanner = null;
            Cipher DESCipher;
            SecretKey secretKey;
            File file = null;
            byte[] decodedKey, decryptedBytes;
            BufferedWriter bufferedWriter;

            path = Paths.get("").toAbsolutePath().toString();
            scanner = new Scanner(System.in);

            System.out.print("File to decrypt: ");
            dataFileName = scanner.nextLine();
            file = new File(dataFileName);

            while(!(file.exists() && !file.isDirectory())) {
                System.out.println("Error. File was not found.\n");
                System.out.print("File to decrypt: ");

                dataFileName = scanner.nextLine();
                file = new File(dataFileName);
            }

            System.out.print("Key file for decrypt: ");
            keyFileName = scanner.nextLine();
            file = new File(keyFileName);

            while(!(file.exists() && !file.isDirectory())) {
                System.out.println("Error. File was not found.\n");
                System.out.print("Key file for decrypt: ");

                keyFileName = scanner.nextLine();
                file = new File(keyFileName);
            }

            decodedKey = Base64.getDecoder().decode(
                new String(Files.readAllBytes(
                    Paths.get(keyFileName))));

            secretKey = new SecretKeySpec(
                decodedKey, 0, decodedKey.length, "DES");

            DESCipher = Cipher.getInstance("DES");
            DESCipher.init(Cipher.DECRYPT_MODE, secretKey);

            decryptedBytes = DESCipher.doFinal(
                Base64.getDecoder().decode(
                    new String(Files.readAllBytes(
                        Paths.get(dataFileName)))));

            /* Write content to file */
        } catch (Exception e) {
            /* Throw error */
            System.exit(1);
        }
    }

    public static void main(String[] args) {
        // Encrypt or decrypt?
    }
}
