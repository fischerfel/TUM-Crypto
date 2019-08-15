public class Helper {

public Cipher dcipher, ecipher;

// Responsible for setting, initializing this object's encrypter and
// decrypter Chipher instances
public Helper(String passPhrase) {

    // 8-bytes Salt
    byte[] salt = {(byte) 0xA9, (byte) 0x9B, (byte) 0xC8, (byte) 0x32, (byte) 0x56, (byte) 0x34, (byte) 0xE3, (byte) 0x03};

    // Iteration count
    int iterationCount = 19;

    try {
        // Generate a temporary key. In practice, you would save this key
        // Encrypting with DES Using a Pass Phrase
        KeySpec keySpec = new PBEKeySpec(passPhrase.toCharArray(), salt, iterationCount);
        SecretKey key = SecretKeyFactory.getInstance("PBEWithMD5AndDES").generateSecret(keySpec);

        ecipher = Cipher.getInstance(key.getAlgorithm());

        // Prepare the parameters to the cipthers
        AlgorithmParameterSpec paramSpec = new PBEParameterSpec(salt, iterationCount);
        ecipher.init(Cipher.ENCRYPT_MODE, key, paramSpec);            

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

// Encrpt Password
@SuppressWarnings("unused")
public String encrypt(String str) {
    try {
        // Encode the string into bytes using utf-8
        byte[] utf8 = str.getBytes("UTF8");
        System.out.println("\n UTF8 : " + utf8);
        // Encrypt
        byte[] enc = ecipher.doFinal(utf8);
        System.out.println("\n enc: " + enc);
        // Encode bytes to base64 to get a string
        return new sun.misc.BASE64Encoder().encode(enc);

    } catch (BadPaddingException e) {
    } catch (IllegalBlockSizeException e) {
    } catch (UnsupportedEncodingException e) {
    }
    return null;
}


public static void main(String[] args) {
    try {

        Helper encrypter = new Helper("");

        System.out.print("Enter a password : ");
        String password = input.nextLine();

        String encrypted = encrypter.encrypt(password);
        System.out.println("encrypted String:" + encrypted);
    } catch (Exception e) {
    }

}
