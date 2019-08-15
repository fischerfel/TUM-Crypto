import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

// You have to download and add it into your project, others are Java Inbuilt Libraries
// Do not use sun.misc for Base64 functions
import org.apache.commons.codec.binary.Base64; 

public class BasicDecryption {

//I've done it in post method of servlet

protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    String encryptedData = request.getParameter("cipherData");
    String data[] = encryptedData.split(":");
    String iv = data[0];
    byte[] encryptedByteData = hexStringToByteArray(data[1]);
    String keyString = data[2];

    IvParameterSpec iv = new IvParameterSpec(Base64.decodeBase64(iv);
    Key k = new SecretKeySpec(Base64.decodeBase64(keyString),"AES");

    try {
        System.out.println("Decrypted String:"+BasicDecryption.decrypt(Base64.encodeBase64String(encryptedByteData),k,iv));
    } catch (InvalidKeyException | NoSuchAlgorithmException
            | NoSuchPaddingException | IllegalBlockSizeException
            | BadPaddingException | InvalidAlgorithmParameterException e) {
        e.printStackTrace();
    }
}

public static final String decrypt(final String encrypted,final Key key, final IvParameterSpec iv) throws InvalidKeyException,
NoSuchAlgorithmException, NoSuchPaddingException,
IllegalBlockSizeException, BadPaddingException, IOException, InvalidAlgorithmParameterException {

      Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
      cipher.init(Cipher.DECRYPT_MODE, key,iv);
      byte[] raw = Base64.decodeBase64(encrypted);
      byte[] stringBytes = cipher.doFinal(raw);
      String clearText = new String(stringBytes, "UTF8");
      return clearText;
}

public static byte[] hexStringToByteArray(String s) {

    int len = s.length();
    byte[] data = new byte[len / 2];

    for (int i = 0; i < len; i += 2) {
        data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                + Character.digit(s.charAt(i+1), 16));
    }
    return data;
}
}
