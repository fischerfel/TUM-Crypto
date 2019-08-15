 import java.io.BufferedWriter;
 import java.io.File;
 import java.io.FileNotFoundException;
 import java.io.FileOutputStream;
 import java.io.IOException;
 import java.io.OutputStreamWriter;
 import java.io.UnsupportedEncodingException;
 import java.io.Writer;
 import java.nio.file.Files;
 import java.nio.file.Paths;
 import java.nio.file.StandardOpenOption;
 import java.security.NoSuchAlgorithmException;
 import java.security.Security;

 import javax.crypto.Cipher;
 import javax.crypto.KeyGenerator;
 import javax.crypto.SecretKey;
 import javax.crypto.spec.IvParameterSpec;
 import javax.crypto.spec.SecretKeySpec;

 import org.apache.commons.io.FileUtils;
 import org.bouncycastle.jce.provider.BouncyCastleProvider;


public class SecTest {

public static void main(String[] args) throws NoSuchAlgorithmException {
       /** Generate a secret TripleDES encryption/decryption key */
Security.addProvider(new BouncyCastleProvider());
    KeyGenerator keygen = KeyGenerator.getInstance("Blowfish");
    // Use it to generate a key
    SecretKey key = keygen.generateKey();
    // Convert the secret key to an array of bytes like this
    byte[] rawKey = key.getEncoded();

    // Write the raw key to the file
    String keyPath = "/data2/key/BlowFish.key";
    FileOutputStream out = null;
try {
    out = new FileOutputStream(keyPath);
} catch (FileNotFoundException e) {
    // TODO Auto-generated catch block
    e.printStackTrace();
}
    Writer writer = null;
try {
    writer = new BufferedWriter(new OutputStreamWriter(out, "UTF-8"));
    Files.write( Paths.get(keyPath),rawKey,StandardOpenOption.CREATE);
    writer.close();
    out.close();
} catch (UnsupportedEncodingException e1) {
    e1.printStackTrace();
} catch (IOException e) {
    e.printStackTrace();
}

generateHexCode("a");
}

private static void generateHexCode(String pwd) {
String hexCipher = null;
try {
        byte[] byteClearText = pwd.getBytes("UTF-8");
        byte[] ivBytes = hexToBytes("0000000000000000");
        // read secretkey from key file

        byte[] secretKeyByte = readSecretKey().getBytes();
        Cipher cipher = null;
        SecretKeySpec key = new SecretKeySpec(secretKeyByte, "Blowfish");
        // Create and initialize the encryption engine
        cipher = Cipher.getInstance("Blowfish/CBC/ZeroBytePadding", "BC");

        IvParameterSpec ivSpec = new IvParameterSpec(ivBytes);
        cipher.init(Cipher.ENCRYPT_MODE, key, ivSpec); // throws exception
        byte[] cipherText = new byte[cipher.getOutputSize(byteClearText.length)];
        int ctLength = cipher.update(byteClearText, 0, byteClearText.length, cipherText, 0);
        ctLength += cipher.doFinal(cipherText, ctLength);
        hexCipher = bytesToHex(cipherText);// hexdecimal password stored in DB
        System.out.println("hex cipher is "+hexCipher);
    } catch (Exception e) {
        e.printStackTrace();
    }

}


private static String readSecretKey() {
byte[] rawkey = null;
    String file ="";
        // Read the raw bytes from the keyfile
        String keyFile = "/data2/key/BlowFish.key";
        String is = null;
    try {
    is = FileUtils.readFileToString(new File(keyFile),"UTF-8");
    } catch (IOException e) {
    // TODO Auto-generated catch block
    e.printStackTrace();
    }

    return is;
}

public static byte[] hexToBytes(String str) {
    byte[] bytes = null;
    if (str != null && str.length() >= 2) {
        int len = str.length() / 2;
        byte[] buffer = new byte[len];
        for (int i = 0; i < len; i++) {
            buffer[i] = (byte) Integer.parseInt(str.substring(i * 2, i * 2 + 2), 16);
        }
        bytes = buffer;
    }
    return bytes;
}

public static String bytesToHex(byte[] data) {
    if (data == null) {
        return null;
    } else {
        int len = data.length;
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < len; i++) {
            if ((data[i] & 0xFF) < 16) {
                str = str.append("0").append(java.lang.Integer.toHexString(data[i] & 0xFF));
            } else {
                str.append(java.lang.Integer.toHexString(data[i] & 0xFF));
            }
        }
        return str.toString().toUpperCase();
    }
}
}
