public class KeywordsCipher {

private static final String PADDING = "DESede/ECB/NoPadding";
private static final String UTF_F8 = "UTF-8";
private static final String DE_SEDE = "DESede";
private String secretKey;

{...}

public String encrypt(String message, String secretKey) {

    byte[] cipherText = null;

    try {
        final byte[] secretBase64Key = Base64.decodeBase64(secretKey);
        final SecretKey key = new SecretKeySpec(secretBase64Key, DE_SEDE);
        final Cipher cipher = Cipher.getInstance(PADDING);
        cipher.init(Cipher.ENCRYPT_MODE, key);
        final byte[] plainTextBytes = message.getBytes();
        cipherText = cipher.doFinal(plainTextBytes);
    } catch (NoSuchAlgorithmException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException | NoSuchPaddingException e) {
        throw new CipherException("Problem with encryption occured");
    }

    return Hex.encodeHexString(cipherText);
}

public CipherKeywordModel decrypt(String keyToDecrypt, String secretKey) {

    try {
        byte[] message = DatatypeConverter.parseHexBinary(keyToDecrypt);
        final byte[] secretBase64Key = Base64.decodeBase64(secretKey);
        final SecretKey key = new SecretKeySpec(secretBase64Key, DE_SEDE);
        final Cipher decipher = Cipher.getInstance(PADDING);
        decipher.init(Cipher.DECRYPT_MODE, key);
        final byte[] plainText = decipher.doFinal(message);
        String decryptedText = new String(plainText, UTF_F8);
    } catch (UnsupportedEncodingException | NoSuchAlgorithmException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException | NoSuchPaddingException e) {
        throw new CipherException("Problem with encryption occured");
    }
    return decryptedText;
}
