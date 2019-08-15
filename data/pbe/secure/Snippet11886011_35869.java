import java.io.FileOutputStream;
import java.io.IOException;
import java.security.AlgorithmParameters;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.security.Security;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.InvalidParameterSpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.EncryptedPrivateKeyInfo;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;

import org.bouncycastle.asn1.bc.BCObjectIdentifiers;

public class EncodePKCS8 {

/**
 * @param args
 * @throws NoSuchAlgorithmException 
 * @throws InvalidKeySpecException 
 * @throws NoSuchPaddingException 
 * @throws InvalidAlgorithmParameterException 
 * @throws InvalidKeyException 
 * @throws BadPaddingException 
 * @throws IllegalBlockSizeException 
 * @throws InvalidParameterSpecException 
 * @throws IOException 
 * @throws NoSuchProviderException 
 */
public static void main(String[] args) throws NoSuchAlgorithmException,
InvalidKeySpecException, NoSuchPaddingException, InvalidKeyException,
InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException,
InvalidParameterSpecException, IOException, NoSuchProviderException
{
    // before we can do anything with BouncyCastle we have to register its provider
    Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());

    String password = "Very long and complex password";

    // generate RSA key pair
    KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
    keyPairGenerator.initialize(2048);
    KeyPair keyPair = keyPairGenerator.genKeyPair();

    byte[] encryptedPkcs8 = encryptPrivateKey(password, keyPair);

    FileOutputStream fos = new FileOutputStream("privkey.p8");
    fos.write(encryptedPkcs8);
    fos.close();

    return;
}

private static byte[] encryptPrivateKey(String password, KeyPair keyPair)
    throws InvalidKeyException, NoSuchAlgorithmException, NoSuchProviderException,
    InvalidKeySpecException, NoSuchPaddingException,
    InvalidAlgorithmParameterException, IllegalBlockSizeException,
    BadPaddingException, InvalidParameterSpecException, IOException
{
    int count = 100000; // hash iteration count, best to leave at default or increase
    return encryptPrivateKey(password, keyPair, count);
}

/**
 * 
 * @param password
 * @param keyPair
 * @param count
 * @return PKCS#8 encoded, encrypted keyPair
 * @throws NoSuchAlgorithmException
 * @throws NoSuchProviderException
 * @throws InvalidKeySpecException
 * @throws NoSuchPaddingException
 * @throws InvalidKeyException
 * @throws InvalidAlgorithmParameterException
 * @throws IllegalBlockSizeException
 * @throws BadPaddingException
 * @throws InvalidParameterSpecException
 * @throws IOException
 */
private static byte[] encryptPrivateKey(String password, 
        KeyPair keyPair, int count) throws NoSuchAlgorithmException,
        NoSuchProviderException, InvalidKeySpecException,
        NoSuchPaddingException, InvalidKeyException,
        InvalidAlgorithmParameterException, IllegalBlockSizeException,
        BadPaddingException, InvalidParameterSpecException, IOException
{
    // extract the encoded private key, this is an unencrypted PKCS#8 private key
        byte[] encodedprivkey = keyPair.getPrivate().getEncoded();

        // Use a PasswordBasedEncryption (PBE) algorithm, OID of this algorithm will be saved
        // in the PKCS#8 file, so changing it (when more standard algorithm or safer
        // algorithm is available) doesn't break backwards compatibility.
        // In other words, decryptor doesn't need to know the algorithm before it will be
        // able to decrypt the PKCS#8 object.
        String encAlg = BCObjectIdentifiers.bc_pbe_sha256_pkcs12_aes256_cbc.getId();

        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);

        // Create PBE parameter set
        PBEParameterSpec pbeParamSpec = new PBEParameterSpec(salt, count);
        PBEKeySpec pbeKeySpec = new PBEKeySpec(password.toCharArray());
        SecretKeyFactory keyFac = SecretKeyFactory.getInstance(encAlg, "BC");
        SecretKey pbeKey = keyFac.generateSecret(pbeKeySpec);

        Cipher pbeCipher = Cipher.getInstance(encAlg, "BC");

        // Initialize PBE Cipher with key and parameters
        pbeCipher.init(Cipher.ENCRYPT_MODE, pbeKey, pbeParamSpec);

        // Encrypt the encoded Private Key with the PBE key
        byte[] ciphertext = pbeCipher.doFinal(encodedprivkey);

        // Now construct  PKCS #8 EncryptedPrivateKeyInfo object
        AlgorithmParameters algparms = AlgorithmParameters.getInstance(encAlg, "BC");
        algparms.init(pbeParamSpec);
        EncryptedPrivateKeyInfo encinfo = new EncryptedPrivateKeyInfo(algparms, ciphertext);

        // DER encoded PKCS#8 encrypted key
        byte[] encryptedPkcs8 = encinfo.getEncoded();

        return encryptedPkcs8;
    }
}
