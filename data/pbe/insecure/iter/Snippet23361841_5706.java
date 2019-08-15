private final static String algorithm = "PBKDF2WithHmacSHA1";

private final static String HEX = "0123456789ABCDEF";

private static final String CP_ALGORITH = "AES";
private static final String CP_KEY = "PUTsomeKEYinHere";

public static String cipher(String cipherKey, String data) throws NoSuchAlgorithmException, 
                    InvalidKeySpecException, NoSuchPaddingException, InvalidKeyException, 
                    IllegalBlockSizeException, BadPaddingException {
    SecretKeyFactory skf = SecretKeyFactory.getInstance(algorithm);
    KeySpec spec = new PBEKeySpec(cipherKey.toCharArray(), cipherKey.getBytes(), 128, 256);
    SecretKey tmp = skf.generateSecret(spec);
    SecretKey key = new SecretKeySpec(tmp.getEncoded(), CP_ALGORITH);
    Cipher cipher = Cipher.getInstance(CP_ALGORITH);
    cipher.init(Cipher.ENCRYPT_MODE, key);
    return toHex(cipher.doFinal(data.getBytes()));
}

public static String decipher(String cipherKey, String data) throws NoSuchAlgorithmException, 
                        InvalidKeySpecException, NoSuchPaddingException, InvalidKeyException, 
                        IllegalBlockSizeException, BadPaddingException {
    SecretKeyFactory skf = SecretKeyFactory.getInstance(algorithm);
    KeySpec spec = new PBEKeySpec(cipherKey.toCharArray(), cipherKey.getBytes(), 128, 256);
    SecretKey tmp = skf.generateSecret(spec);
    SecretKey key = new SecretKeySpec(tmp.getEncoded(), CP_ALGORITH);
    Cipher cipher = Cipher.getInstance(CP_ALGORITH);
    cipher.init(Cipher.DECRYPT_MODE, key);
    return new String(cipher.doFinal(toByte(data)));
}

private static byte[] toByte(String data) throws NullPointerException{
    int len = data.length()/2;
    byte[] result = new byte[len];
    for (int i = 0; i < len; i++)
        result[i] = Integer.valueOf(data.substring(2*i, 2*i+2), 16).byteValue();
    return result;
}

private static String toHex(byte[] doFinal) {
    StringBuffer result = new StringBuffer(2*doFinal.length);
    for (int i = 0; i < doFinal.length; i++) {
        result.append(HEX.charAt((doFinal[i]>>4)&0x0f)).append(HEX.charAt(doFinal[i]&0x0f));
    }
    return result.toString();
}
