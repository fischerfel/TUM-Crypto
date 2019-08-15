public class RSAService {

/**
 * String to hold name of the encryption algorithm.
 */
public static final String ALGORITHM = "RSA";

/**
 * String to hold the name of the private key file.
 */
public static final String PRIVATE_KEY_FILE = "private.key";

/**
 * String to hold name of the public key file.
 */
public static final String PUBLIC_KEY_FILE = "public.key";

/**
 * Generate key which contains a pair of private and public key using 1024
 * bytes. Store the set of keys in Prvate.key and Public.key files.
 *
 */
public static void generateKey() {

    try {
        final KeyPairGenerator keyGen = KeyPairGenerator.getInstance(ALGORITHM);
        keyGen.initialize(1024);
        final KeyPair key = keyGen.generateKeyPair();

        File privateKeyFile = new File(PRIVATE_KEY_FILE);
        File publicKeyFile = new File(PUBLIC_KEY_FILE);

        // Create files to store public and private key
        privateKeyFile.createNewFile();

        if (publicKeyFile.getParentFile() != null) {
            publicKeyFile.getParentFile().mkdirs();
        }
        publicKeyFile.createNewFile();

        // Saving the Public key in a file
        ObjectOutputStream publicKeyOS = new ObjectOutputStream(
                new FileOutputStream(publicKeyFile));
        publicKeyOS.writeObject(key.getPublic());
        publicKeyOS.close();

        // Saving the Private key in a file
        ObjectOutputStream privateKeyOS = new ObjectOutputStream(
                new FileOutputStream(privateKeyFile));
        privateKeyOS.writeObject(key.getPrivate());
        privateKeyOS.close();
    } catch (Exception e) {
        e.printStackTrace();
    }

}

/**
 * The method checks if the pair of public and private key has been
 * generated.
 *
 * @return flag indicating if the pair of keys were generated.
 */
public static boolean areKeysPresent() {

    File privateKey = new File(PRIVATE_KEY_FILE);
    File publicKey = new File(PUBLIC_KEY_FILE);

    if (privateKey.exists() && publicKey.exists()) {
        return true;
    }
    return false;
}

/**
 * Encrypt the plain text using public key.
 *
 * @param text : original plain text
 * @param key :The public key
 * @return Encrypted text
 * @throws java.lang.Exception
 */
public static byte[] encrypt(String text, PublicKey key) {
    byte[] cipherText = null;
    try {
        // get an RSA cipher object and print the provider
        final Cipher cipher = Cipher.getInstance(ALGORITHM);
        // encrypt the plain text using the public key
        cipher.init(Cipher.ENCRYPT_MODE, key);
        cipherText = cipher.doFinal(text.getBytes());
    } catch (Exception e) {
        e.printStackTrace();
    }
    return cipherText;
}

/**
 * Decrypt text using private key.
 *
 * @param text :encrypted text
 * @param key :The private key
 * @return plain text
 * @throws java.lang.Exception
 */
public static String decrypt(byte[] text, PrivateKey key) {
    byte[] dectyptedText = null;
    try {
        // get an RSA cipher object and print the provider
        final Cipher cipher = Cipher.getInstance(ALGORITHM);

        // decrypt the text using the private key
        cipher.init(Cipher.DECRYPT_MODE, key);
        dectyptedText = cipher.doFinal(text);

    } catch (Exception ex) {
        ex.printStackTrace();
    }

    return new String(dectyptedText);
}

public static String doEncryption(String input) {


    try {

        if (!RSAService.areKeysPresent()) {
            RSAService.generateKey();
        }


        ObjectInputStream inputStream;

        // Encrypt the string using the public key
        inputStream = new ObjectInputStream(new FileInputStream(PUBLIC_KEY_FILE));
        PublicKey publicKey = (PublicKey) inputStream.readObject();
        byte[] cipherText = RSAService.encrypt(input, publicKey);
        return cipherText.toString();

    } catch (Exception e) {
        e.printStackTrace();
    }
    return "ERROR: Public key file is probably missing";
}


public static String doDecryption(String input) {

           try {

        if (!RSAService.areKeysPresent()) {
            RSAService.generateKey();
        }

        ObjectInputStream inputStream;


        // Decrypt the cipher text using the private key.
        inputStream = new ObjectInputStream(new FileInputStream(PRIVATE_KEY_FILE));
        PrivateKey privateKey = (PrivateKey) inputStream.readObject();
        String out = decrypt(input.getBytes(), privateKey);
        return out;

    } catch (Exception e) {
        e.printStackTrace();
    }
    return "ERROR: Private key file is probably missing or doesn't match the public key";
}
