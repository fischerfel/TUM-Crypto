import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;
/**
 * 
 * @param password
 * @return
 * @throws AuthException 
 */
public static String encryptPassword(String password) throws AuthException{
    byte[] textEncrypted = "".getBytes();
    try{

        DESKeySpec keySpec = new DESKeySpec(Constants.DESkey);
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
        SecretKey myDesKey = keyFactory.generateSecret(keySpec);

        Cipher desCipher;

        // Create the cipher 
        //desCipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
        desCipher = Cipher.getInstance("DES/CBC/PKCS5Padding");

        AlgorithmParameterSpec alogrithm_specs = new IvParameterSpec(Constants.DESkey);

        // Initialize the cipher for encryption
        desCipher.init(Cipher.ENCRYPT_MODE, myDesKey, alogrithm_specs);

        //sensitive information
        byte[] text = password.getBytes();

        // Encrypt the text
        textEncrypted = desCipher.doFinal(text);

    }catch(NoSuchAlgorithmException e){
        e.printStackTrace();
        logger.error("[AUTH] Auth Exception...there is something wrong at the encryption phase\n" + e.getMessage());
        throw new AuthException("Auth Exception...there is something wrong at the encryption phase");
    }catch(NoSuchPaddingException e){
        e.printStackTrace();
        logger.error("[AUTH] Auth Exception...there is something wrong at the encryption phase\n" + e.getMessage());
        throw new AuthException("Auth Exception...there is something wrong at the encryption phase");
    }catch(InvalidKeyException e){
        e.printStackTrace();
        logger.error("[AUTH] Auth Exception...there is something wrong at the encryption phase\n" + e.getMessage());
        throw new AuthException("Auth Exception...there is something wrong at the encryption phase");
    }catch(IllegalBlockSizeException e){
        e.printStackTrace();
        logger.error("[AUTH] Auth Exception...there is something wrong at the encryption phase\n" + e.getMessage());
        throw new AuthException("Auth Exception...there is something wrong at the encryption phase");
    }catch(BadPaddingException e){
        e.printStackTrace();
        logger.error("[AUTH] Auth Exception...there is something wrong at the encryption phase\n" + e.getMessage());
        throw new AuthException("Auth Exception...there is something wrong at the encryption phase");
    } catch (InvalidKeySpecException e) {
        e.printStackTrace();
        logger.error("[AUTH] Auth Exception...there is something wrong at the encryption phase\n" + e.getMessage());
        throw new AuthException("Auth Exception...there is something wrong at the encryption phase");
    } catch (InvalidAlgorithmParameterException e) {
        e.printStackTrace();
        logger.error("[AUTH] Auth Exception...there is something wrong at the encryption phase\n" + e.getMessage());
        throw new AuthException("Auth Exception...there is something wrong at the encryption phase");
    } 

    return new String(textEncrypted);

}   
public static String decryptPassword(String passwordToDecrypt) throws AuthException{

    DESKeySpec keySpec;
    byte[] textDecrypted = "".getBytes();
    try {
        keySpec = new DESKeySpec(Constants.DESkey);

        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
        SecretKey myDesKey = keyFactory.generateSecret(keySpec);

        Cipher desCipher;

        AlgorithmParameterSpec alogrithm_specs = new IvParameterSpec(Constants.DESkey);

        // Create the cipher 
        //desCipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
        desCipher = Cipher.getInstance("DES/CBC/PKCS5Padding");

        // Initialize the same cipher for decryption
        desCipher.init(Cipher.DECRYPT_MODE, myDesKey, alogrithm_specs);

        // Decrypt the text
        byte[] passwordToDecryptByte = passwordToDecrypt.getBytes();



       textDecrypted = desCipher.doFinal(passwordToDecryptByte);

    } catch (InvalidKeyException e) {
        e.printStackTrace();
        logger.error("[AUTH] Auth Exception...there is something wrong at the encryption phase\n" + e.getMessage());
        throw new AuthException("Auth Exception...there is something wrong at the decryption phase");
    } catch (NoSuchAlgorithmException e) {
        e.printStackTrace();
        logger.error("[AUTH] Auth Exception...there is something wrong at the encryption phase\n" + e.getMessage());
        throw new AuthException("Auth Exception...there is something wrong at the decryption phase");
    } catch (InvalidKeySpecException e) {
        e.printStackTrace();
        logger.error("[AUTH] Auth Exception...there is something wrong at the encryption phase\n" + e.getMessage());
        throw new AuthException("Auth Exception...there is something wrong at the decryption phase");
    } catch (NoSuchPaddingException e) {
        e.printStackTrace();
        logger.error("[AUTH] Auth Exception...there is something wrong at the encryption phase\n" + e.getMessage());
        throw new AuthException("Auth Exception...there is something wrong at the decryption phase");
    } catch (IllegalBlockSizeException e) {
        e.printStackTrace();
        logger.error("[AUTH] Auth Exception...there is something wrong at the encryption phase\n" + e.getMessage());
        throw new AuthException("Auth Exception...there is something wrong at the decryption phase");
    } catch (BadPaddingException e) {
        e.printStackTrace();
        logger.error("Auth Exception...there is something wrong at the encryption phase\n" + e.getMessage());
        throw new AuthException("Auth Exception...there is something wrong at the decryption phase");
    } catch (InvalidAlgorithmParameterException e) {
        e.printStackTrace();
        logger.error("[AUTH] Auth Exception...there is something wrong at the encryption phase\n" + e.getMessage());
        throw new AuthException("Auth Exception...there is something wrong at the decryption phase");
    }

    return new String(textDecrypted);

}


public static void main(String[] args) throws AuthException, UnsupportedEncodingException{
    String password = URLEncoder.encode(encryptPassword("bnlbnl18"), "UTF-8");

    System.out.println("\"" + URLDecoder.decode(password, "UTF-8") + "\"" + decryptPassword(URLDecoder.decode(password,"UTF-8")));

}
