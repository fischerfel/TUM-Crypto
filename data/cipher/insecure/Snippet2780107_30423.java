public class CryptoHelper {
private static final String TAG = "CryptoHelper";
//private static final String PBEWithSHA256And256BitAES = "PBEWithSHA256And256BitAES-CBC-BC";
//private static final String PBEWithSHA256And256BitAES = "PBEWithMD5And128BitAES-CBC-OpenSSL";
private static final String PBEWithSHA256And256BitAES = "PBEWithMD5And128BitAES-CBC-OpenSSLPBEWITHSHA1AND3-KEYTRIPLEDES-CB";
private static final String randomAlgorithm = "SHA1PRNG";
public static final int SALT_LENGTH = 8;
public static final int SALT_GEN_ITER_COUNT = 20;
private final static String HEX = "0123456789ABCDEF";

private Cipher e_Cipher; 
private Cipher d_Cipher;
private SecretKey secretKey;
private byte salt[];

public CryptoHelper(String password) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidAlgorithmParameterException, InvalidKeySpecException {
    char[] cPassword = password.toCharArray();
    PBEKeySpec pbeKeySpec = new PBEKeySpec(cPassword);
    PBEParameterSpec pbeParamSpec = new PBEParameterSpec(salt, SALT_GEN_ITER_COUNT);
    SecretKeyFactory keyFac = SecretKeyFactory.getInstance(PBEWithSHA256And256BitAES);
    secretKey = keyFac.generateSecret(pbeKeySpec);

    SecureRandom saltGen = SecureRandom.getInstance(randomAlgorithm);
    this.salt = new byte[SALT_LENGTH];
    saltGen.nextBytes(this.salt);

    e_Cipher = Cipher.getInstance(PBEWithSHA256And256BitAES);
    d_Cipher = Cipher.getInstance(PBEWithSHA256And256BitAES);

    e_Cipher.init(Cipher.ENCRYPT_MODE, secretKey, pbeParamSpec);
    d_Cipher.init(Cipher.DECRYPT_MODE, secretKey, pbeParamSpec);
}

public String encrypt(String cleartext) throws IllegalBlockSizeException, BadPaddingException {
    byte[] encrypted = e_Cipher.doFinal(cleartext.getBytes());

    return convertByteArrayToHex(encrypted);
}

public String decrypt(String cipherString) throws IllegalBlockSizeException {
    byte[] plainText = decrypt(convertStringtobyte(cipherString));

    return(new String(plainText));
}

public byte[] decrypt(byte[] ciphertext) throws IllegalBlockSizeException {        
    byte[] retVal = {(byte)0x00};
    try {
        retVal = d_Cipher.doFinal(ciphertext);
    } catch (BadPaddingException e) {
        Log.e(TAG, e.toString()); 
    }
    return retVal;
}


public String convertByteArrayToHex(byte[] buf) {
    if (buf == null)  
        return "";
    StringBuffer result = new StringBuffer(2*buf.length);  

    for (int i = 0; i < buf.length; i++) {
        appendHex(result, buf[i]);  
    }
    return result.toString();
}

private static void appendHex(StringBuffer sb, byte b) {
    sb.append(HEX.charAt((b>>4)&0x0f)).append(HEX.charAt(b&0x0f));
}

private static byte[] convertStringtobyte(String hexString) {
    int len = hexString.length()/2;
    byte[] result = new byte[len];
    for (int i = 0; i < len; i++) {
        result[i] = Integer.valueOf(hexString.substring(2*i, 2*i+2), 16).byteValue();
    }
    return result;
}

public byte[] getSalt() {
    return salt;
}

public SecretKey getSecretKey() {
    return secretKey;
}

public static SecretKey createSecretKey(char[] password) throws NoSuchAlgorithmException, InvalidKeySpecException {
    PBEKeySpec pbeKeySpec = new PBEKeySpec(password);
    SecretKeyFactory keyFac = SecretKeyFactory.getInstance(PBEWithSHA256And256BitAES);
    return keyFac.generateSecret(pbeKeySpec);
}
