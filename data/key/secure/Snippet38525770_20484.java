public class EncryptionUtils {

private static String ALGO = "AES";
private static  Cipher cipher;




public static String encrypt(String message, String keyString) {
    cipher = Cipher.getInstance(ALGO);
        Key key = generateKey(keyString);
        cipher.init(Cipher.ENCRYPT_MODE, key);
        return new String(BASE64EncoderStream.encode(cipher.doFinal( message.getBytes())));
}

public static String decrypt(String message, String keyString)  {

       cipher = Cipher.getInstance(ALGO);
        Key key = generateKey(keyString);
        cipher.init(Cipher.DECRYPT_MODE, key);
        return new String(cipher.doFinal(BASE64DecoderStream.decode(message.getBytes()))); 

}

private static Key generateKey(String keyString) throws NoSuchAlgorithmException {
    byte[] keyBytes = BASE64DecoderStream.decode(keyString.getBytes());
    Key key = new SecretKeySpec(keyBytes, ALGO);
    return key;
}

public static void main(String args[]) {
    byte[] keyValue = new byte[16];
    new SecureRandom().nextBytes(keyValue);
    String key = new String(BASE64EncoderStream.encode(keyValue));
    String message = "test message";
    String enc = encrypt(message, key);
    String dec = decrypt(enc, key);
    System.out.println(dec);
}}
