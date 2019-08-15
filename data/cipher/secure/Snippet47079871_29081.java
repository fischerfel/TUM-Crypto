package javatest;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.Security;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openssl.PEMKeyPair;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;

/**
 * Decrypt class
 *
 * @link http://coding.westreicher.org/?p=23
 * @author a.chernyy
 */
public class RSA {

    /**
     * Singleton class object RSA
     */
    private static volatile RSA instance;

    /**
     * Private key
     */
    private final PrivateKey privateKey;

    /**
     * Cipher
     */
    private final Cipher cipher;

    /**
     * Constructor
     *
     * @throws NoSuchAlgorithmException
     * @throws NoSuchPaddingException
     */
    private RSA(String privateKeyPath) throws NoSuchAlgorithmException, NoSuchPaddingException, FileNotFoundException, IOException {
        //create cipher
        this.cipher = Cipher.getInstance("RSA");

        //get private key
        File keyFl = new File(privateKeyPath);
        Security.addProvider(new BouncyCastleProvider());
        PEMParser pemParser = new PEMParser(new InputStreamReader(new FileInputStream(keyFl)));
        PEMKeyPair pemKeyPair = (PEMKeyPair) pemParser.readObject();
        JcaPEMKeyConverter converter = new JcaPEMKeyConverter().setProvider("BC");
        this.privateKey = converter.getPrivateKey(pemKeyPair.getPrivateKeyInfo());
    }

    //Static methods
    /**
     * Static method getInstance return single refer on object RSA. If object
     * not exists, it will be created
     *
     * @param privateKeyPath
     * @throws NoSuchAlgorithmException
     * @throws NoSuchPaddingException
     * @throws java.io.FileNotFoundException
     * @return RSA
     */
    public static RSA getInstance(String privateKeyPath) throws NoSuchAlgorithmException, NoSuchPaddingException, FileNotFoundException, IOException {
        if (instance == null) {
            synchronized (RSA.class) {
                if (instance == null) {
                    instance = new RSA(privateKeyPath);
                }
            }
        }
        return instance;
    }

    /**
     * Block chipher
     *
     * @param bytes
     * @param mode
     * @return
     * @throws IllegalBlockSizeException
     * @throws BadPaddingException
     */
    private byte[] blockCipher(byte[] bytes, int mode) throws IllegalBlockSizeException, BadPaddingException {
        // string initialize 2 buffers.
        // scrambled will hold intermediate results
        byte[] scrambled = new byte[0];

        // toReturn will hold the total result
        byte[] toReturn = new byte[0];
        // if we encrypt we use 100 byte long blocks. Decryption requires 128 byte long blocks (because of RSA)
        int length = (mode == Cipher.ENCRYPT_MODE) ? 100 : 128;

        // another buffer. this one will hold the bytes that have to be modified in this step
        byte[] buffer = new byte[length];

        for (int i = 0; i < bytes.length; i++) {

            // if we filled our buffer array we have our block ready for de- or encryption
            if ((i > 0) && (i % length == 0)) {
                //execute the operation
                scrambled = cipher.doFinal(buffer);
                // add the result to our total result.
                toReturn = append(toReturn, scrambled);
                // here we calculate the length of the next buffer required
                int newlength = length;

                // if newlength would be longer than remaining bytes in the bytes array we shorten it.
                if (i + length > bytes.length) {
                    newlength = bytes.length - i;
                }
                // clean the buffer array
                buffer = new byte[newlength];
            }
            // copy byte into our buffer.
            buffer[i % length] = bytes[i];
        }

        // this step is needed if we had a trailing buffer. should only happen when encrypting.
        // example: we encrypt 110 bytes. 100 bytes per run means we "forgot" the last 10 bytes. they are in the buffer array
        scrambled = cipher.doFinal(buffer);

        // final step before we can return the modified data.
        toReturn = append(toReturn, scrambled);

        return toReturn;
    }

    /**
     * Concatinate bytes
     *
     * @param prefix
     * @param suffix
     * @return
     */
    private byte[] append(byte[] prefix, byte[] suffix) {
        byte[] toReturn = new byte[prefix.length + suffix.length];
        for (int i = 0; i < prefix.length; i++) {
            toReturn[i] = prefix[i];
        }
        for (int i = 0; i < suffix.length; i++) {
            toReturn[i + prefix.length] = suffix[i];
        }
        return toReturn;
    }

    public void decrypt(String filePath, String fileDecryptPath) throws Exception {
        //Convert file into bytes
        this.cipher.init(Cipher.DECRYPT_MODE, this.privateKey);
        File encryptedFile = new File(filePath);
        FileInputStream isEncryptedFile = new FileInputStream(encryptedFile);
        byte encryptedFileData[] = new byte[(int) encryptedFile.length()];
        isEncryptedFile.read(encryptedFileData);
        byte[] bts = encryptedFileData;

        //decrypt
        byte[] decrypted = blockCipher(bts, Cipher.DECRYPT_MODE);

        //Push decrypted data into file
        //...
    }
}
