private static final String UNICODE_FORMAT = "UTF8";
public static final String DESEDE_ENCRYPTION_SCHEME = "DESede";
private KeySpec myKeySpec;
private SecretKeyFactory mySecretKeyFactory;
private Cipher cipher;
byte[] keyAsBytes;
private String myEncryptionKey;
private String myEncryptionScheme;
SecretKey key;

public DESedeEncryption() throws Exception
{
    myEncryptionKey = "YOURPRIVATEKEY";
    myEncryptionScheme = DESEDE_ENCRYPTION_SCHEME;
    keyAsBytes = myEncryptionKey.getBytes(UNICODE_FORMAT);
    myKeySpec = new DESedeKeySpec(keyAsBytes);
    mySecretKeyFactory = SecretKeyFactory.getInstance(myEncryptionScheme);
    cipher = Cipher.getInstance(myEncryptionScheme);
    key = mySecretKeyFactory.generateSecret(myKeySpec);
}

/**
 * Method To Encrypt The String
 */
public String encrypt(String unencryptedString) {
    String encryptedString = null;
    try {
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] plainText = unencryptedString.getBytes(UNICODE_FORMAT);
        byte[] encryptedText = cipher.doFinal(plainText);
        encryptedString = Base64.encodeToString(encryptedText, Base64.DEFAULT);
    } catch (Exception e) {
        e.printStackTrace();
    }
    return encryptedString.endsWith("\n") ? encryptedString.replace("\n","") : encryptedString;
}
/**
 * Method To Decrypt An Ecrypted String
 */
public String decrypt(String encryptedString) {
    String decryptedText=null;
    try {
        cipher.init(Cipher.DECRYPT_MODE, key);
        byte[] encryptedText = Base64.decode(encryptedString, Base64.DEFAULT);
        byte[] plainText = cipher.doFinal(encryptedText);
        decryptedText= bytes2String(plainText);
    } catch (Exception e) {
        e.printStackTrace();
    }
    return decryptedText;
}
/**
 * Returns String From An Array Of Bytes
 */
private static String bytes2String(byte[] bytes) {
    StringBuffer stringBuffer = new StringBuffer();
    for (int i = 0; i > bytes.length; i++) {
        stringBuffer.append((char) bytes[i]);
    }
    return stringBuffer.toString();
}
