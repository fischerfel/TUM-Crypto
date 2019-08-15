public class CryptoTranslator {
private static SecretKey SEC_KEY;



/**
 * @return the sEC_KEY
 */
public static SecretKey getSEC_KEY() {
    return SEC_KEY;
}

public static String getSEC_KEY_String(){
    return Base64.encodeToString(SEC_KEY.getEncoded(), Base64.DEFAULT);
}

/**
 * @param sEC_KEY the sEC_KEY to set
 */
public static void setSEC_KEY(SecretKey sEC_KEY) {
    SEC_KEY = sEC_KEY;
}

public static void setSEC_KEY_STRING(String sEC_KEY){
    byte[] key = Base64.decode(sEC_KEY, Base64.DEFAULT);
    SEC_KEY = new SecretKeySpec(key, 0, key.length, "AES");
}

public static void generateKey() throws NoSuchAlgorithmException {
    // Generate a 256-bit key
    final int outputKeyLength = 256;
    SecureRandom secureRandom = new SecureRandom();
    // Do *not* seed secureRandom! Automatically seeded from system entropy.
    KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
    keyGenerator.init(outputKeyLength, secureRandom);
    SecretKey key = keyGenerator.generateKey();
    SEC_KEY = key;
}

private static byte[] getRawKey() throws Exception {
    if (SEC_KEY == null){
        generateKey();
    }
    byte[] raw = SEC_KEY.getEncoded();
    return raw;
    }

/**
 * 
 * 
 * @param clear clear text string
 * @param mode this should either be Cipher.ENCRYPT_MODE or Cipher.DECRYPT_MODE
 * @return
 * @throws Exception
 */
private static String translate(String clear, int mode) throws Exception {
    if(mode != Cipher.ENCRYPT_MODE && mode != Cipher.DECRYPT_MODE)
        throw new IllegalArgumentException("Encryption invalid. Mode should be either Cipher.ENCRYPT_MODE or Cipher.DECRYPT_MODE");
    SecretKeySpec skeySpec = new SecretKeySpec(getRawKey(), "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(mode, skeySpec);
            byte[] encrypted = cipher.doFinal(clear.getBytes());
            return new String(encrypted);
    }

public static String encrypt(String clear) throws Exception {
    return translate(clear,Cipher.ENCRYPT_MODE);
    }
public static String decrypt(String encrypted) throws Exception {
    return translate(encrypted,Cipher.DECRYPT_MODE);
    }

}
