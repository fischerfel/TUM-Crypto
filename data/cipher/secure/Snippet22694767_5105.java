import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.SecureRandom;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class crypt {

    public static void main(String args[]) throws Exception {


        String keyString = "averylongtext!@$@#$#@$#&(&}{23432432432dsfsdf";
        FileWriter fileWriter = null, fileWriter1 = null;
        File enc = new File("C:\\test\\encrypted.txt");
        File dec = new File("C:\\test\\decrypted.txt");
        String path = "C:\\test\\normal_file.txt";
        String path2 = "C:\\test\\encrypted.txt";
        fileWriter = new FileWriter(enc);
        fileWriter1 = new FileWriter(dec);


        String input = readFile(path, StandardCharsets.UTF_8);
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        byte[] iv = new byte[cipher.getBlockSize()];
        new SecureRandom().nextBytes(iv);
        IvParameterSpec ivSpec = new IvParameterSpec(iv);
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        digest.update(keyString.getBytes());
        byte[] key = new byte[16];
        System.arraycopy(digest.digest(), 0, key, 0, key.length);
        SecretKeySpec keySpec = new SecretKeySpec(key, "AES");
        cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);
        byte[] encrypted = cipher.doFinal(input.getBytes());
        System.out.println(new String(encrypted));

   //writing encrypted information to a new file encrypted.txt
        fileWriter.write(new String(encrypted));
        fileWriter.close();

  //reading encrypted information from the file encrypted.txt
  //This part is where the error is
        encrypted = readFile(path2, StandardCharsets.UTF_8).getBytes();
        cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);
        byte[] decrypted = cipher.doFinal(encrypted);
        System.out.println("decrypted: \n" + new String(decrypted, "UTF-8"));

  //writing the decrypted information to the file decrypted.txt
        fileWriter1.write(new String(decrypted));
        fileWriter1.close();

    }

 //method to read a file
    static String readFile(String path, Charset encoding) throws IOException {
        byte[] encoded = Files.readAllBytes(Paths.get(path));
        return encoding.decode(ByteBuffer.wrap(encoded)).toString();
    }

}
