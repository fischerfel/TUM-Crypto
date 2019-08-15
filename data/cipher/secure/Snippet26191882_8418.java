public class Aes { 
private static final int pswdIterations = 10;
private static final int keySize =  128;

public static String encrypt(String plainText, String password, String salt, String initializationVector) throws NoSuchAlgorithmException, NoSuchPaddingException, DecoderException, InvalidKeySpecException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException  {
             Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
             SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
             KeySpec spec = new PBEKeySpec(password.toCharArray(), Hex.decodeHex(salt.toCharArray()), pswdIterations, keySize);
             SecretKey key = new SecretKeySpec(factory.generateSecret(spec).getEncoded(), "AES");
             cipher.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(Hex.decodeHex(initializationVector.toCharArray())));
             byte[] encryptedTextBytes = cipher.doFinal(plainText.getBytes());
             return new Base64().encodeAsString(encryptedTextBytes);
} 
}
