/**
 * 
 * @param password
 * @return
 * @throws AuthException 
 * @throws UnsupportedEncodingException 
 */
public static String encryptPassword(String password) throws AuthException, UnsupportedEncodingException{
    byte[] textEncrypted = "".getBytes();
    try{

        DESKeySpec keySpec = new DESKeySpec(Constants.DESkey);
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
        SecretKey myDesKey = keyFactory.generateSecret(keySpec);

        Cipher desCipher;

        // Create the cipher 
        desCipher = Cipher.getInstance("DES/CBC/PKCS5Padding");

        AlgorithmParameterSpec alogrithm_specs = new IvParameterSpec(Constants.DESkey);

        // Initialize the cipher for encryption
        desCipher.init(Cipher.ENCRYPT_MODE, myDesKey, alogrithm_specs);

        //sensitive information
        byte[] text = password.getBytes("UTF-8");

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

    return BaseEncoding.base64().encode(textEncrypted);

}   

/**
 * 
 * @param passwordDecrypted
 * @return
 * @throws AuthException 
 * @throws UnsupportedEncodingException 
 */

public static String decryptPassword(String passwordToDecrypt) throws AuthException, UnsupportedEncodingException{

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
        byte[] passwordToDecryptByte = BaseEncoding.base64().decode(passwordToDecrypt);

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
    String password = encryptPassword("testtest19");

    System.out.println(password + " --> " + "\"" + decryptPassword(password)+ "\"");

}
