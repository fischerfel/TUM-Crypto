public class Utils {
private Cipher ecipher;


public Utils() {

    String passPhrase = "PASSWORD";

    byte[] salt = { ...};

    int iterationCount = 1024;

    try {

        KeySpec keySpec = new PBEKeySpec(passPhrase.toCharArray(), salt,iterationCount);
        SecretKey key = SecretKeyFactory.getInstance("PBEWithMD5AndDES").generateSecret(keySpec);

        ecipher = Cipher.getInstance(key.getAlgorithm());
        dcipher = Cipher.getInstance(key.getAlgorithm());

        AlgorithmParameterSpec paramSpec = new PBEParameterSpec(salt, iterationCount);

        ecipher.init(Cipher.ENCRYPT_MODE, key, paramSpec);
        dcipher.init(Cipher.DECRYPT_MODE, key, paramSpec);

    } catch (InvalidAlgorithmParameterException e) {
        System.out.println("EXCEPTION: InvalidAlgorithmParameterException");
    } catch (InvalidKeySpecException e) {
        System.out.println("EXCEPTION: InvalidKeySpecException");
    } catch (NoSuchPaddingException e) {
        System.out.println("EXCEPTION: NoSuchPaddingException");
    } catch (NoSuchAlgorithmException e) {
        System.out.println("EXCEPTION: NoSuchAlgorithmException");
    } catch (InvalidKeyException e) {
        System.out.println("EXCEPTION: InvalidKeyException");
    }
}

public String encrypt(String str) {
    try {
        byte[] utf8 = str.getBytes("UTF-8");
        byte[] enc = ecipher.doFinal(utf8);
        return new String(Base64.encodeBase64(enc), "UTF-8");
    } catch (BadPaddingException e) {
    } catch (IllegalBlockSizeException e) {
    } catch (UnsupportedEncodingException e) {
    } catch (Exception e) {
    }
    return null;
}
