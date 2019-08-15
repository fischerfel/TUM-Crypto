import java.security.*;
import java.security.spec.*;
import javax.crypto.*;
import javax.crypto.spec.*;
import java.io.*;
import java.util.*;

/**
 *  This class encrypts and decrypts a file using CipherStreams
 *  and a 256-bit Rijndael key. The key is then encrypted using
 *  a 1024-bit RSA key, which is password-encrypted.
 */
public class FileEncryptorRSA {
  /**
   *    When files are encrypted, this will be appended to the end
   *    of the filename.
   */
  private static final String ENCRYPTED_FILENAME_SUFFIX=".encrypted";

  /**
   *    When files are decrypted, this will be appended to the end
   *    of the filename.
   */
  private static final String DECRYPTED_FILENAME_SUFFIX=".decrypted";

  /**
   *    Number of times the password will be hashed with MD5
   *    when transforming it into a TripleDES key.
   */
  private static final int ITERATIONS = 1000;

  /**
   *    FileEncryptor is started with one of three options:
   *
   *    -c: create key pair and write it to 2 files
   *    -e: encrypt a file, given as an argument
   *    -d: decrypt a file, given as an argument
   */
  public static void main (String[] args)
  throws Exception {
    if ((args.length < 1) || (args.length > 2)) {
      usage();
    } else if ("-c".equals(args[0])) {
      createKey();
    } else if ("-e".equals(args[0])) {
      encrypt(args[1]);
    } else if ("-d".equals(args[0])) {
      decrypt(args[1]);
    } else {
      usage();
    }
  }

  private static void usage() {
    System.err.println("Usage: java FileEncryptor -c|-e|-d [filename]");
    System.exit(1);
  }

  /**
   *    Creates a 1024 bit RSA key and stores it to
   *    the filesystem as two files.
   */
  private static void createKey()
  throws Exception {
    BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
    System.out.print("Password to encrypt the private key: ");
    String password = in.readLine();
    System.out.println("Generating an RSA keypair...");

    // Create an RSA key
    KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
    keyPairGenerator.initialize(1024);
    KeyPair keyPair = keyPairGenerator.genKeyPair();

    System.out.println("Done generating the keypair.\n");

    // Now we need to write the public key out to a file
    System.out.print("Public key filename: ");
    String publicKeyFilename = in.readLine();

    // Get the encoded form of the public key so we can
    // use it again in the future. This is X.509 by default.
    byte[] publicKeyBytes = keyPair.getPublic().getEncoded();

    // Write the encoded public key out to the filesystem
    FileOutputStream fos = new FileOutputStream(publicKeyFilename);
    fos.write(publicKeyBytes);
    fos.close();

    // Now we need to do the same thing with the private key,
    // but we need to password encrypt it as well.
    System.out.print("Private key filename: ");
    String privateKeyFilename = in.readLine();

    // Get the encoded form. This is PKCS#8 by default.
    byte[] privateKeyBytes = keyPair.getPrivate().getEncoded();

    // Here we actually encrypt the private key
    byte[] encryptedPrivateKeyBytes =
    passwordEncrypt(password.toCharArray(),privateKeyBytes);

    fos = new FileOutputStream(privateKeyFilename);
    fos.write(encryptedPrivateKeyBytes);
    fos.close();
  }

  /**
   *    Encrypt the given file with a session key encrypted with an
   *    RSA public key which will be read in from the filesystem.
   */
  private static void encrypt(String fileInput)
  throws Exception {

    BufferedReader in = new BufferedReader
    (new InputStreamReader(System.in));
    System.out.print("Public Key to encrypt with: ");
    String publicKeyFilename = in.readLine();

    // Load the public key bytes
    FileInputStream fis = new FileInputStream(publicKeyFilename);
    ByteArrayOutputStream baos = new ByteArrayOutputStream();

    int theByte = 0;
    while ((theByte = fis.read()) != -1)
    {
      baos.write(theByte);
    }
    fis.close();

    byte[] keyBytes = baos.toByteArray();
    baos.close();

    // Turn the encoded key into a real RSA public key.
    // Public keys are encoded in X.509.
    X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
    KeyFactory keyFactory = KeyFactory.getInstance("RSA");
    PublicKey publicKey = keyFactory.generatePublic(keySpec);

    // Open up an output file for the output of the encryption
    String fileOutput = fileInput + ENCRYPTED_FILENAME_SUFFIX;
    DataOutputStream output = new DataOutputStream
    (new FileOutputStream(fileOutput));

    // Create a cipher using that key to initialize it
    Cipher rsaCipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
    rsaCipher.init(Cipher.ENCRYPT_MODE, publicKey);

    // Now create a new 256 bit Rijndael key to encrypt the file itself.
    // This will be the session key.
    KeyGenerator rijndaelKeyGenerator = KeyGenerator.getInstance("Rijndael");
    rijndaelKeyGenerator.init(256);
    System.out.println("Generating session key...");
    Key rijndaelKey = rijndaelKeyGenerator.generateKey();
    System.out.println("Done generating key.");

    // Encrypt the Rijndael key with the RSA cipher
    // and write it to the beginning of the file.
    byte[] encodedKeyBytes= rsaCipher.doFinal(rijndaelKey.getEncoded());
    output.writeInt(encodedKeyBytes.length);
    output.write(encodedKeyBytes);

    // Now we need an Initialization Vector for the symmetric cipher in CBC mode
    SecureRandom random = new SecureRandom();
    byte[] iv = new byte[16];
    random.nextBytes(iv);

    // Write the IV out to the file.
    output.write(iv);
    IvParameterSpec spec = new IvParameterSpec(iv);

    // Create the cipher for encrypting the file itself.
    Cipher symmetricCipher = Cipher.getInstance("Rijndael/CBC/PKCS5Padding");
    symmetricCipher.init(Cipher.ENCRYPT_MODE, rijndaelKey, spec);

    CipherOutputStream cos = new CipherOutputStream(output, symmetricCipher);

    System.out.println("Encrypting the file...");

    FileInputStream input = new FileInputStream(fileInput);

    theByte = 0;
    while ((theByte = input.read()) != -1)
    {
      cos.write(theByte);
    }
    input.close();
    cos.close();
    System.out.println("File encrypted.");
    return;
  }

  /**
   *    Decrypt the given file.
   *    Start by getting the RSA private key
   *    and decrypting the session key embedded
   *    in the file. Then decrypt the file with
   *    that session key.
   */
  private static void decrypt(String fileInput)
  throws Exception {

    BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
    System.out.print("Private Key to decrypt with: ");
    String privateKeyFilename = in.readLine();

    System.out.print("Password for the private key: ");
    String password = in.readLine();

    // Load the private key bytes
    FileInputStream fis = new FileInputStream(privateKeyFilename);
    ByteArrayOutputStream baos = new ByteArrayOutputStream();

    int theByte = 0;
    while ((theByte = fis.read()) != -1)
    {
      baos.write(theByte);
    }
    fis.close();

    byte[] keyBytes = baos.toByteArray();
    baos.close();

    keyBytes = passwordDecrypt(password.toCharArray(), keyBytes);

    // Turn the encoded key into a real RSA private key.
    // Private keys are encoded in PKCS#8.
    PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
    KeyFactory keyFactory = KeyFactory.getInstance("RSA");
    PrivateKey privateKey = keyFactory.generatePrivate(keySpec);

    // Create a cipher using that key to initialize it
    Cipher rsaCipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");

    // Read in the encrypted bytes of the session key
    DataInputStream dis = new DataInputStream(new FileInputStream(fileInput));
    byte[] encryptedKeyBytes = new byte[dis.readInt()];
    dis.readFully(encryptedKeyBytes);

    // Decrypt the session key bytes.
    rsaCipher.init(Cipher.DECRYPT_MODE, privateKey);
    byte[] rijndaelKeyBytes = rsaCipher.doFinal(encryptedKeyBytes);

    // Transform the key bytes into an actual key.
    SecretKey rijndaelKey = new SecretKeySpec(rijndaelKeyBytes, "Rijndael");

    // Read in the Initialization Vector from the file.
    byte[] iv = new byte[16];
    dis.read(iv);
    IvParameterSpec spec = new IvParameterSpec(iv);

    Cipher cipher = Cipher.getInstance("Rijndael/CBC/PKCS5Padding");
    cipher.init(Cipher.DECRYPT_MODE, rijndaelKey, spec);
    CipherInputStream cis = new CipherInputStream(dis, cipher);

    System.out.println("Decrypting the file...");
    FileOutputStream fos = new FileOutputStream(fileInput + DECRYPTED_FILENAME_SUFFIX);

    // Read through the file, decrypting each byte.
    theByte = 0;
    while ((theByte = cis.read()) != -1)
    {
      fos.write(theByte);
    }
    cis.close();
    fos.close();
    System.out.println("Done.");
    return;
  }

  /**
   *    Utility method to encrypt a byte array with a given password.
   *    Salt will be the first 8 bytes of the byte array returned.
   */
  private static byte[] passwordEncrypt(char[] password, byte[] plaintext) throws Exception {

    // Create the salt.
    byte[] salt = new byte[8];
    Random random = new Random();
    random.nextBytes(salt);

    // Create a PBE key and cipher.
    PBEKeySpec keySpec = new PBEKeySpec(password);
    SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("PBEWithSHAAndTwofish-CBC");
    SecretKey key = keyFactory.generateSecret(keySpec);
    PBEParameterSpec paramSpec = new PBEParameterSpec(salt, ITERATIONS);
    Cipher cipher = Cipher.getInstance("PBEWithSHAAndTwofish-CBC");
    cipher.init(Cipher.ENCRYPT_MODE, key, paramSpec);

    // Encrypt the array
    byte[] ciphertext = cipher.doFinal(plaintext);

    // Write out the salt, then the ciphertext and return it.
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    baos.write(salt);
    baos.write(ciphertext);
    return baos.toByteArray();
  }

  /**
   *    Utility method to decrypt a byte array with a given password.
   *    Salt will be the first 8 bytes in the array passed in.
   */
  private static byte[] passwordDecrypt(char[] password, byte[] ciphertext) throws Exception {

    // Read in the salt.
    byte[] salt = new byte[8];
    ByteArrayInputStream bais = new ByteArrayInputStream(ciphertext);
    bais.read(salt,0,8);

    // The remaining bytes are the actual ciphertext.
    byte[] remainingCiphertext = new byte[ciphertext.length-8];
    bais.read(remainingCiphertext,0,ciphertext.length-8);

    // Create a PBE cipher to decrypt the byte array.
    PBEKeySpec keySpec = new PBEKeySpec(password);
    SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("PBEWithSHAAndTwofish-CBC");
    SecretKey key = keyFactory.generateSecret(keySpec);
    PBEParameterSpec paramSpec = new PBEParameterSpec(salt, ITERATIONS);
    Cipher cipher = Cipher.getInstance("PBEWithSHAAndTwofish-CBC");

    // Perform the actual decryption.
    cipher.init(Cipher.DECRYPT_MODE, key, paramSpec);
    return cipher.doFinal(remainingCiphertext);
  }
}
