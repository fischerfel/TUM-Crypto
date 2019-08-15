import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import org.apache.log4j.Logger;
import com.test.exceptions.EncryptionException;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public final class StringEncrypter {

  private KeySpec keySpec;
  private SecretKeyFactory keyFactory;
  private Cipher cipher;

  private static final Logger LOGGER = Logger.getLogger("com.test.util.StringEncrypter");


  public StringEncrypter(String anEncryptionKey) throws EncryptionException {

    if (anEncryptionKey == null) {
      throw new EncryptionException(new InvalidKeyException("encryption key was null"));
    }
    else if (anEncryptionKey.trim().length() < 24) {
      throw new EncryptionException(new InvalidKeyException("encryption key was less than 24 characters"));
    }
    else {

      try {

        byte[] keyAsBytes = anEncryptionKey.getBytes("UTF-8");

        keySpec = new DESKeySpec(keyAsBytes);

        keyFactory = SecretKeyFactory.getInstance("DES");

        cipher = Cipher.getInstance("DES");

      }
      catch (InvalidKeyException ike) {
        throw new EncryptionException(ike);
      }
      catch (UnsupportedEncodingException uee) {
        throw new EncryptionException(uee);
      }
      catch (NoSuchAlgorithmException nsae) {
        throw new EncryptionException(nsae);
      }
      catch (NoSuchPaddingException nspee) {
        throw new EncryptionException(nspee);
      }
    }
  }


  public String encrypt(String anUnencryptedString) throws EncryptionException {
    if (anUnencryptedString == null || anUnencryptedString.trim().length() == 0) {
      throw new EncryptionException(new InvalidKeyException("unencrypted string was null or empty"));
    }
    else {
      try {
        SecretKey secretKey = keyFactory.generateSecret(keySpec);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] cleartext = anUnencryptedString.getBytes("UTF-8");


        byte[] ciphertext = cipher.doFinal(cleartext);

        BASE64Encoder base64encoder = new BASE64Encoder();
        return base64encoder.encode(ciphertext);
      }
      catch (InvalidKeySpecException ikse) {
        throw new EncryptionException(ikse);
      }
      catch (InvalidKeyException ike) {
        throw new EncryptionException(ike);
      }
      catch (UnsupportedEncodingException uee) {
        throw new EncryptionException(uee);
      }
      catch (IllegalBlockSizeException ibse) {
        throw new EncryptionException(ibse);
      }
      catch (BadPaddingException bpee) {
        throw new EncryptionException(bpee);
      }
    }
  }


  public String decrypt(String anEncryptedString) throws EncryptionException {
    if (anEncryptedString == null || anEncryptedString.trim().length() <= 0) {
      throw new EncryptionException(new InvalidKeyException("encrypted string was null or empty"));
    }
    else {
      try {
        SecretKey secretKey = keyFactory.generateSecret(keySpec);

        cipher.init(Cipher.DECRYPT_MODE, secretKey);

        BASE64Decoder base64decoder = new BASE64Decoder();

        byte[] cleartext = base64decoder.decodeBuffer(anEncryptedString);

        byte[] ciphertext = cipher.doFinal(cleartext);

        return bytesToString(ciphertext);
      }
      catch (InvalidKeySpecException ikse) {
        throw new EncryptionException(ikse);
      }
      catch (InvalidKeyException ike) {
        throw new EncryptionException(ike);
      }
      catch (IllegalBlockSizeException ibse) {
        throw new EncryptionException(ibse);
      }
      catch (BadPaddingException bpee) {
          bpee.printStackTrace();
        throw new EncryptionException(bpee);
      }
      catch (IOException ioe) {
        throw new EncryptionException(ioe);
      }
    }
  }

  private static String bytesToString(byte[] someBytes) {
    StringBuffer stringBuffer = new StringBuffer();
    for (int i = 0; i < someBytes.length; i++) {
      stringBuffer.append((char) someBytes[i]);
    }
    return stringBuffer.toString();
  }
}
