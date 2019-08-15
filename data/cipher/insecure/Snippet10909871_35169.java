import java.security.*;
import java.*;
import java.io.*;
import javax.crypto.*;
import javax.crypto.spec.*;

public class Test{
  public static void main(String[] args) throws Exception {
    String key = "80f28a1ef4aa9df6ee2ee3210316b98f383eb344";

    // Init the key
    DESKeySpec desKeySpec = new DESKeySpec(key.getBytes());
    SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
    Key secretKey = keyFactory.generateSecret(desKeySpec);

    Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
    cipher.init(Cipher.DECRYPT_MODE, secretKey);

    byte[] buf = new byte[1024];
    InputStream input = new FileInputStream(new File("enc.txt"));
    FileOutputStream output = new FileOutputStream(new File("dec.txt"));

    int count = input.read(buf);

    // Read and decrypt file content
    while (count >= 0) {
        output.write(cipher.update(buf, 0, count)); 
        count = input.read(buf);        
    }
    output.write(cipher.doFinal());
    output.flush();

  }
}
