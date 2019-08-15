import java.awt.image.*;
import java.io.*;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import sun.misc.*;
import javax.swing.*;

/**
*
* @author Lance Gerday
*/
public class Encrypt {

   private static final String ALGORITHM = "AES";
   public static byte[] keyValue;
   // 500 KB max
   public static byte[] valuesRead = new byte[512000];

   public static void encrypt(File f) throws Exception {
       FileInputStream in = null;
       FileOutputStream out = null;
       in = new FileInputStream(f);
       Key key = generateKey();
       Cipher c = Cipher.getInstance(ALGORITHM);
       c.init(Cipher.ENCRYPT_MODE, key);//my code seems to fail here


       String name = f.getName();
       String newFileName = name.substring(0, name.lastIndexOf("."))
               + ".enc" + name.substring(name.lastIndexOf("."), name.length());
       File newFile = new File(f.getParentFile(), newFileName);
       out = new FileOutputStream(newFile);
       //reads the file into valueToEnc and returns the number of bytes read
       valuesRead = new byte[Integer.MAX_VALUE];
       int numberRead = in.read(valuesRead);
       keyValue = new byte[numberRead];
       for (int i = 0; i < numberRead; i++) {
           keyValue[i] = valuesRead[i];
       }
       byte[] encValue = c.doFinal(keyValue);
       String encryptedValue = new BASE64Encoder().encode(encValue);
       out.write(encryptedValue.getBytes());
   }

   public static void decrypt(File f) throws Exception {
       Key key = generateKey();
       Cipher c = Cipher.getInstance(ALGORITHM);
       c.init(Cipher.DECRYPT_MODE, key);

       FileInputStream in = null;
       FileOutputStream out = null;

       if (f.canRead()) {
           in = new FileInputStream(f);
       }

       String name = f.getName();
       String newFileName = name.substring(0, name.lastIndexOf(".enc"));
       File newFile = new File(f.getParentFile(), newFileName);
       out = new FileOutputStream(newFile);
       //reads the file into valueToEnc and returns the number of bytes read
       valuesRead = new byte[Integer.MAX_VALUE];
       int numberRead = in.read(valuesRead);
       keyValue = new byte[numberRead];
       for (int i = 0; i < numberRead; i++) {
           keyValue[i] = valuesRead[i];
       }
       String encryptedValue = new String(keyValue);
       byte[] decordedValue = new BASE64Decoder().decodeBuffer(encryptedValue);
       byte[] decValue = c.doFinal(decordedValue);
       out.write(decValue);
   }

   private static Key generateKey() throws Exception {
       Key key = new SecretKeySpec(keyValue, ALGORITHM);
       return key;
   }

   public static void setKeyValue(File f) {
       BufferedImage img = null;
       try {
           img = javax.imageio.ImageIO.read(f);
       } catch (Exception e) {
           JOptionPane.showMessageDialog(null, "Fail error at line 92");
       }
       Raster r = img.getData();
       int[] data = r.getPixels(r.getMinX(), r.getMinY(), r.getWidth(), r.getHeight(), (int[]) null);
       for (int a : data) {
       }
       int dataLength = data.length;
       keyValue = new byte[dataLength << 2];

       for (int i = 0; i < dataLength; i++) {
           int x = data[i];
           int k = i << 2;
           keyValue[k++] = (byte) ((x >>> 0) & 0xff);
           keyValue[k++] = (byte) ((x >>> 8) & 0xff);
           keyValue[k++] = (byte) ((x >>> 16) & 0xff);
           keyValue[k++] = (byte) ((x >>> 24) & 0xff);
       }
   }
}
