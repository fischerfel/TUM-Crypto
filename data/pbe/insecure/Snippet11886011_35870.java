import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.AlgorithmParameters;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Security;
import java.security.interfaces.RSAKey;
import java.security.interfaces.RSAPrivateCrtKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.security.spec.RSAPublicKeySpec;

import javax.crypto.Cipher;
import javax.crypto.EncryptedPrivateKeyInfo;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public class DecodePKCS8 {

    /**
     * @param args
     * @throws IOException 
     * @throws NoSuchPaddingException When file is corrupted
     * @throws NoSuchAlgorithmException When no BC provider has been loaded 
     * @throws InvalidKeySpecException When decryption of file failed
     * @throws InvalidAlgorithmParameterException When file is corrupted
     * @throws InvalidKeyException When Unlimited cryptography extensions are not installed
     */
    public static void main(String[] args) throws
    IOException, NoSuchAlgorithmException, NoSuchPaddingException,
    InvalidKeySpecException, InvalidKeyException, InvalidAlgorithmParameterException
    {
        // before we can do anything with BouncyCastle we have to register its provider
        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());

        String password = "Very long and complex password";

        // read DER encoded key from files
        byte[] encodedprivkey = getFileBytes("privkey.p8");

        // this is a encoded PKCS#8 encrypted private key
        EncryptedPrivateKeyInfo ePKInfo = new EncryptedPrivateKeyInfo(encodedprivkey);

        // first we have to read algorithm name and parameters (salt, iterations) used
        // to encrypt the file
        Cipher cipher = Cipher.getInstance(ePKInfo.getAlgName());
        PBEKeySpec pbeKeySpec = new PBEKeySpec(password.toCharArray());

        SecretKeyFactory skFac = SecretKeyFactory.getInstance(ePKInfo
                .getAlgName());
        Key pbeKey = skFac.generateSecret(pbeKeySpec);

        // Extract the iteration count and the salt
        AlgorithmParameters algParams = ePKInfo.getAlgParameters();
        cipher.init(Cipher.DECRYPT_MODE, pbeKey, algParams);

        // Decrypt the encryped private key into a PKCS8EncodedKeySpec
        KeySpec pkcs8KeySpec = ePKInfo.getKeySpec(cipher);

        // Now retrieve the RSA Public and private keys by using an
        // RSA key factory.
        KeyFactory rsaKeyFac = KeyFactory.getInstance("RSA");
        // First get the private key
        PrivateKey rsaPriv = rsaKeyFac.generatePrivate(pkcs8KeySpec);
        // Now derive the RSA public key from the private key
        RSAPublicKeySpec rsaPubKeySpec = new RSAPublicKeySpec(((RSAKey) rsaPriv).getModulus(),
                ((RSAPrivateCrtKey) rsaPriv).getPublicExponent());
        PublicKey rsaPubKey = (RSAPublicKey) rsaKeyFac.generatePublic(rsaPubKeySpec);

    System.out.println("Key extracted, public part: " + rsaPubKey);
    }

    private static byte[] getFileBytes(String path)
    {
        File f = new File(path);
        int sizecontent = ((int) f.length()); // no key file will ever be bigger than 4GiB...
        byte[] data = new byte[sizecontent];
        try 
            {
            FileInputStream freader = new FileInputStream(f);
            freader.read(data, 0, sizecontent) ;
            freader.close();
            return data;
            }
        catch(IOException ioe)
        {
            System.out.println(ioe.toString());
            return null;
        }
    }
}
