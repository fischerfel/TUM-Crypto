 /**
 * Generate the private key using the passed string.
 * 
 * @param keyGeneratorString
 *            : The string which is to be used to generate the private key.
 * @return : SecretKey else null.
 */
public SecretKey getKey(String keyGeneratorString) {
    SecretKeyFactory keyFactory = null;
    DESKeySpec keySpec = null;
    try {
        // only the first 8 Bytes of the constructor argument are used
        // as material for generating the keySpec
        keySpec = new DESKeySpec(keyGeneratorString.getBytes("UTF-8"));
        // Get the DES encryption standard instance
        keyFactory = SecretKeyFactory.getInstance("DES");
        // Generate and return the key.
        return keyFactory.generateSecret(keySpec);
    } catch (UnsupportedEncodingException uee) {
        logger.error("****** Error while generating key : "
                + uee.getMessage());
    } catch (InvalidKeyException ike) {
        logger.error("****** Error while generating key : "
                + ike.getMessage());
    } catch (NoSuchAlgorithmException e) {
        logger.error("****** Error while generating key : "
                + e.getMessage());
    } catch (InvalidKeySpecException e) {
        logger.error("****** Error while generating key : "
                + e.getMessage());
    }
    // There was error while generating the key hence return null.
    return null;
}

/**
 * Encrypt the string using the SecretKey.
 * 
 * @param stringToBeEncrypted
 *            : The String to be encrypted.
 * @param key
 *            : The secret key to be used for encryption.
 * @return : Encrypted byte[] or null.
 */
public byte[] encrypt(String stringToBeEncrypted, SecretKey key) {
    Cipher cipherInst;
    try {
        cipherInst = Cipher.getInstance("DES/ECB/PKCS5Padding");
        cipherInst.init(Cipher.ENCRYPT_MODE, key);// cipher is not thread
                                                    // safe
        byte[] encrypted = cipherInst.doFinal(stringToBeEncrypted
                .getBytes());
        return encrypted;
    } catch (NoSuchAlgorithmException e) {
        logger.error("****** Error while encrypting : "
                + e.getMessage());
    } catch (NoSuchPaddingException e) {
        logger.error("****** Error while encrypting : "
                + e.getMessage());
    } catch (InvalidKeyException e) {
        logger.error("****** Error while encrypting : "
                + e.getMessage());
    } catch (IllegalBlockSizeException e) {
        logger.error("****** Error while encrypting : "
                + e.getMessage());
    } catch (BadPaddingException e) {
        logger.error("****** Error while encrypting : "
                + e.getMessage());
    }
    return null;
}

/**
 * Decrypt the string using the SecretKey.
 * 
 * @param stringToBeDecrypted : byte[] to be decrypted.
 * @param key : The secret key to be used for decryption.
 * @return : Decrypted byte[] or null.
 */
public byte[] decrypt(byte[] stringToBeDecrypted, SecretKey key) {
    Cipher cipherInst;
    try {
        cipherInst = Cipher.getInstance("DES/ECB/PKCS5Padding");
        cipherInst.init(Cipher.DECRYPT_MODE, key);
        byte[] original = cipherInst
                .doFinal(stringToBeDecrypted);
        return original;
    } catch (NoSuchAlgorithmException e) {
        logger.error("****** Error while decrypting : "
                + e.getMessage());
    } catch (NoSuchPaddingException e) {
        logger.error("****** Error while decrypting : "
                + e.getMessage());
    } catch (InvalidKeyException e) {
        logger.error("****** Error while decrypting : "
                + e.getMessage());
    } catch (IllegalBlockSizeException e) {
        logger.error("****** Error while decrypting : "
                + e.getMessage());
    } catch (BadPaddingException e) {
        logger.error("****** Error while decrypting : "
                + e.getMessage());
    }
    return null;
}
