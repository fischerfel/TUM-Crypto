//Decrypt

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.security.interfaces.ECPublicKey;
import java.security.spec.ECParameterSpec;
import java.security.spec.ECPoint;
import java.security.spec.ECPublicKeySpec;
import java.util.Base64;
import java.util.Scanner;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.ShortBufferException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.bouncycastle.jce.ECNamedCurveTable;
import org.bouncycastle.jce.spec.ECNamedCurveParameterSpec;

public class Decrypt {

  public static byte[] iv = new SecureRandom().generateSeed(16);


  public static void main(String[] args) {

    /*  Scanner scc = new Scanner(System.in);
        System.out.println("Enter IV:");
        String ivate = scc.nextLine();
        byte[] iv = ivate.getBytes();
    */
    System.out.println("IV=" + iv);

    Scanner sc = new Scanner(System.in); // object for scanner
    System.out.println("Enter your Cipher:");
    String cipherText = sc.nextLine();

    Scanner scanner = new Scanner(System.in); // object for scanner

    System.out.println("Enter your Secret Key B:");

    String encodedKeyB = scanner.nextLine();
    byte[] decodedKeyB = Base64.getDecoder().decode(encodedKeyB);

    SecretKey secretKeyB = new SecretKeySpec(decodedKeyB, 0, decodedKeyB.length, "AES");

    // Initialize two key pairs
    //  KeyPair keyPairA = generateECKeys();
    // KeyPair keyPairB = generateECKeys();

    // Create two AES secret keys to encrypt/decrypt the message
    //SecretKey secretKeyA = generateSharedSecret(keyPairA.getPrivate(),
    //        keyPairB.getPublic());
    //SecretKey secretKeyB = generateSharedSecret(keyPairB.getPrivate(),
    //       keyPairA.getPublic());

    // Encrypt the message using 'secretKeyA'
    //  String cipherText = encryptString(secretKeyA, plainText);
    //   System.out.println("Encrypted cipher text: " + cipherText);

    // Decrypt the message using 'secretKeyB'
    String decryptedPlainText = decryptString(secretKeyB, cipherText);
    System.out.println("Decrypted cipher text: " + decryptedPlainText);
    System.out.println(" cipher text: " + cipherText);
    System.out.println("Key: " + secretKeyB);
  }

  /*  public static KeyPair generateECKeys() {
        try {
            ECNamedCurveParameterSpec parameterSpec = ECNamedCurveTable.getParameterSpec("brainpoolp256r1");
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(
                    "ECDH", "BC");

            keyPairGenerator.initialize(parameterSpec);
            KeyPair keyPair = keyPairGenerator.generateKeyPair();

            return keyPair;
        } catch (NoSuchAlgorithmException | InvalidAlgorithmParameterException
                | NoSuchProviderException e) {
            e.printStackTrace();
            return null;
        }
    }
*/
  /*    public static SecretKey generateSharedSecret(PrivateKey privateKey,
              PublicKey publicKey) {
          try {
              KeyAgreement keyAgreement = KeyAgreement.getInstance("ECDH", "BC");
              keyAgreement.init(privateKey);
              keyAgreement.doPhase(publicKey, true);

              SecretKey key = keyAgreement.generateSecret("AES");
              return key;
          } catch (InvalidKeyException | NoSuchAlgorithmException
                  | NoSuchProviderException e) {
              // TODO Auto-generated catch block
              e.printStackTrace();
              return null;
          }
      }
  */
  /*  public static String encryptString(SecretKey secretkeyB, String plainText) {
        try {
            IvParameterSpec ivSpec = new IvParameterSpec(iv);
            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding", "BC");
            byte[] plainTextBytes = plainText.getBytes("UTF-8");
            byte[] cipherText;

            cipher.init(Cipher.ENCRYPT_MODE, key, ivSpec);
            cipherText = new byte[cipher.getOutputSize(plainTextBytes.length)];
            int encryptLength = cipher.update(plainTextBytes, 0,
                    plainTextBytes.length, cipherText, 0);
            encryptLength += cipher.doFinal(cipherText, encryptLength);

            return bytesToHex(cipherText);
        } catch (NoSuchAlgorithmException | NoSuchProviderException
                | NoSuchPaddingException | InvalidKeyException
                | InvalidAlgorithmParameterException
                | UnsupportedEncodingException | ShortBufferException
                | IllegalBlockSizeException | BadPaddingException e) {
            e.printStackTrace();
            return null;
        }
    }
*/
  public static String decryptString(SecretKey secretkeyB, String cipherText) {
    try {
      Key decryptionKey = new SecretKeySpec(secretkeyB.getEncoded(),
        secretkeyB.getAlgorithm());
      IvParameterSpec ivSpec = new IvParameterSpec(iv);
      Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding", "BC");
      byte[] cipherTextBytes = hexToBytes(cipherText);
      byte[] plainText;


      cipher.init(Cipher.DECRYPT_MODE, decryptionKey, ivSpec);
      plainText = new byte[cipher.getOutputSize(cipherTextBytes.length)];
      int decryptLength = cipher.update(cipherTextBytes, 0,
        cipherTextBytes.length, plainText, 0);
      decryptLength = cipher.doFinal(plainText, decryptLength);

      return new String(plainText, "UTF-8");
    } catch (NoSuchAlgorithmException | NoSuchProviderException |
      NoSuchPaddingException | InvalidKeyException |
      InvalidAlgorithmParameterException |
      IllegalBlockSizeException | BadPaddingException |
      ShortBufferException | UnsupportedEncodingException e) {
      e.printStackTrace();
      return null;
    }
  }

  public static String bytesToHex(byte[] data, int length) {
    String digits = "0123456789ABCDEF";
    StringBuffer buffer = new StringBuffer();

    for (int i = 0; i != length; i++) {
      int v = data[i] & 0xff;

      buffer.append(digits.charAt(v >> 4));
      buffer.append(digits.charAt(v & 0xf));
    }

    return buffer.toString();
  }

  public static String bytesToHex(byte[] data) {
    return bytesToHex(data, data.length);
  }

  public static byte[] hexToBytes(String string) {
    int length = string.length();
    byte[] data = new byte[length / 2];
    for (int i = 0; i < length; i += 2) {
      data[i / 2] = (byte)((Character.digit(string.charAt(i), 16) << 4) + Character
        .digit(string.charAt(i + 1), 16));
    }
    return data;
  }
}
