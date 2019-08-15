public static String encrypt(String key, String value) throws GeneralSecurityException 
{

    byte[] raw = key.getBytes();
    if (raw.length != 16) {
        throw new IllegalArgumentException("Invalid key size.");
    }

    SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
    Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
    cipher.init(Cipher.ENCRYPT_MODE, skeySpec, new IvParameterSpec(new byte[16]));
    byte[] cipherBytes= cipher.doFinal(value.getBytes());
    byte[] encoded =    org.apache.commons.codec.binary.Base64.encodeBase64(cipherBytes);
    return new String(encoded);

}

public static String decrypt(String key, String encrypted) throws GeneralSecurityException 
{

    byte[] raw = key.getBytes();
    if (raw.length != 16) {
        throw new IllegalArgumentException("Invalid key size.");
    }
    SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");

    Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
    cipher.init(Cipher.DECRYPT_MODE, skeySpec, new IvParameterSpec(new byte[16]));
    byte[] byteDecodedText =  org.apache.commons.codec.binary.Base64.decodeBase64(encrypted.getBytes()) ;

    byte[] original = cipher.doFinal(byteDecodedText);

    return new String(original);
}
