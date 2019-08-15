import java.awt.FileDialog;
import java.awt.Frame;
import java.io.*;
import java.nio.file.*;
import java.security.*;
import java.util.*;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;


public class WordAnalysisMenu {

private static KeyGenerator kgen;
private static SecretKey key;
private static byte[] iv = null;

static Scanner sc = new Scanner(System.in);

private static void init(){
    try {
        kgen = KeyGenerator.getInstance("AES");
        kgen.init(128);
        key = kgen.generateKey();
    } catch (NoSuchAlgorithmException e) {

    }
}

public static void main (String [] arge) throws NoSuchAlgorithmException, NoSuchPaddingException, 
        InvalidKeyException, IOException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {

    List<String> warPeace = new ArrayList<>();


    while(true) {

        int choice = menu();

        switch(choice) {
            case 0:
            break;
            case 1:
                //select file for reading
                                warPeace = readFile();
            break;

            case 7:

                //apply the cipher and save it to a file
                                init();
                                Frame frame = new Frame();
                                FileDialog fileDialog = new FileDialog(frame, "", FileDialog.SAVE);
                                fileDialog.setVisible(true);
                                Path path = Paths.get(fileDialog.getDirectory() + fileDialog.getFile());
                                iv = ListEncrypter(warPeace, path, key);


            break;
            case 8:
                // read the ciper file, decode, and print
                                init();
                                frame = new Frame();
                                fileDialog = new FileDialog(frame, "", FileDialog.LOAD);
                                fileDialog.setVisible(true);
                                Path inputFile = Paths.get(fileDialog.getDirectory() + fileDialog.getFile());
                                warPeace = ListDecrypter(inputFile, key, iv);
            break;
            case 9:
                System.out.println("Good bye!");
                System.exit(0);
            break;
            default:
            break;
        }
    }

}

public static int menu() {

    int choice = 0;
    System.out.println("\nPlease make a selection:  ");
    System.out.println("1.\t Select a file for reading.");
    System.out.println("7.\t Apply cipher & save");
    System.out.println("8.\t Read encoded file");
    System.out.println("9.\t Exit");
    Scanner scan = new Scanner(System.in);
    try {
        choice = scan.nextInt();
        scan.nextLine();
    }
    catch(InputMismatchException ime) {
        System.out.println("Invalid input!");
    }
    return choice;
}

    public static List<String> readFile() {

    List<String> word = new ArrayList<String>();

    Frame f = new Frame();
    FileDialog saveBox = new FileDialog(f, "Reading text file", FileDialog.LOAD);
    saveBox.setVisible(true);
    String insName = saveBox.getFile();
    String fileSavePlace = saveBox.getDirectory();

    File inFile = new File(fileSavePlace + insName);

    BufferedReader in = null;
    try {
        in = new BufferedReader(new FileReader(inFile));
        String line;

        while (((line = in.readLine()) != null)) {
                        System.out.println(line);
                        String s = new String();
                        word.add(line);
        }
    } catch (IOException io) {
        System.out.println("There Was An Error Reading The File");
    } finally {
        try {
            in.close();
        } catch (Exception e) {
                        e.getMessage();
        }
    }
    return word;
}

private static byte[] ListEncrypter(List<String> content, Path outputFile, SecretKey key)
        throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IOException, IllegalBlockSizeException, BadPaddingException {
    Cipher encryptCipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            //Cipher encryptCipher = Cipher.getInstance("AES/CFB8/NoPadding");
    encryptCipher.init(Cipher.ENCRYPT_MODE, key);

    StringBuilder sb = new StringBuilder();
    content.stream().forEach(e -> sb.append(e).append(System.lineSeparator()));

    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    CipherOutputStream cipherOutputStream = new CipherOutputStream(outputStream, encryptCipher);
            cipherOutputStream.write(encryptCipher.doFinal(sb.toString().getBytes()));
    //cipherOutputStream.write(sb.toString().getBytes());
    cipherOutputStream.flush();
    cipherOutputStream.close();

    ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
    Files.copy(inputStream, outputFile, StandardCopyOption.REPLACE_EXISTING);


    return encryptCipher.getIV();
}

    private static List<String> ListDecrypter(Path inputFile, SecretKey key, byte[] iv) throws 
           NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, 
           InvalidAlgorithmParameterException, IOException, IllegalBlockSizeException, BadPaddingException {

    Cipher decryptCipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            //Cipher decryptCipher = Cipher.getInstance("AES/CBC/CFB8NoPadding");
    IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);
    decryptCipher.init(Cipher.DECRYPT_MODE, key, ivParameterSpec);

            List<String> fileContent = new ArrayList<>();
            String line = null;
    ByteArrayInputStream inputStream = new ByteArrayInputStream(Files.readAllBytes(inputFile));

    try(CipherInputStream chipherInputStream = new CipherInputStream(new FileInputStream(inputFile.toFile()), decryptCipher);
                    BufferedReader br = new BufferedReader(new InputStreamReader(chipherInputStream))) {

                while ((line = br.readLine()) != null) {
                    System.out.println(line);
                    //fileContent.add(line);
                }
            }


    return null; 

}

}
