import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class Test 
{
    final String ALGORITHM = "AES";                       //symmetric algorithm for data encryption
    final String PADDING_MODE = "/CBC/PKCS5Padding";      //Padding for symmetric algorithm
    final String CHAR_ENCODING = "UTF-8";                 //character encoding
    //final String CRYPTO_PROVIDER = "SunMSCAPI";             //provider for the crypto

    int AES_KEY_SIZE = 256;  //symmetric key size (128, 192, 256) if using 256 you must have the Java Cryptography Extension (JCE) Unlimited Strength Jurisdiction Policy Files  installed

    private String doCrypto(String plainText) throws NoSuchAlgorithmException, NoSuchProviderException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException, UnsupportedEncodingException
    {
        byte[] dataToEncrypt = plainText.getBytes(CHAR_ENCODING);

        //get the symmetric key generator
        KeyGenerator keyGen = KeyGenerator.getInstance(ALGORITHM);
        keyGen.init(AES_KEY_SIZE); //set the key size

        //generate the key
        SecretKey skey = keyGen.generateKey();

        //convert to binary
        byte[] rawAesKey = skey.getEncoded();

        //initialize the secret key with the appropriate algorithm
        SecretKeySpec skeySpec = new SecretKeySpec(rawAesKey, ALGORITHM);

        //get an instance of the symmetric cipher
        Cipher aesCipher = Cipher.getInstance(ALGORITHM + PADDING_MODE);

        //set it to encrypt mode, with the generated key
        aesCipher.init(Cipher.ENCRYPT_MODE, skeySpec);

        //get the initialization vector being used (to be returned)
        byte[] aesIV = aesCipher.getIV();

        //encrypt the data
        byte[] encryptedData = aesCipher.doFinal(dataToEncrypt);    

        //initialize the secret key with the appropriate algorithm
        SecretKeySpec skeySpecDec = new SecretKeySpec(rawAesKey, ALGORITHM);

        //get an instance of the symmetric cipher
        Cipher aesCipherDec = Cipher.getInstance(ALGORITHM +PADDING_MODE);

        //set it to decrypt mode with the AES key, and IV
        aesCipherDec.init(Cipher.DECRYPT_MODE, skeySpecDec, new IvParameterSpec(aesIV));

        //decrypt and return the data
        byte[] decryptedData = aesCipherDec.doFinal(encryptedData);

        return new String(decryptedData, CHAR_ENCODING);
    }

    public static void main(String[] args)
    {
        String text = "Lets encrypt me";

        Test test = new Test();

        try {
            System.out.println(test.doCrypto(text));
        } catch (InvalidKeyException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (NoSuchProviderException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (BadPaddingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
