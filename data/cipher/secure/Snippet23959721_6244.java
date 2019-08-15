public class RSA {
public static void main (String[] args) throws NoSuchAlgorithmException, InvalidKeyException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {

    KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
    kpg.initialize(2048);   // 1024 or 2048
    KeyPair kp = kpg.generateKeyPair();
    Key publicKey = kp.getPublic();
    Key privateKey = kp.getPrivate();
    Cipher cipher = generateCipher();
    String data = "abcdefghijklmnop\0\0\0";

    System.out.println("Plaintext: " + data);
    byte[] ciphertext = rsaEncrypt(data.getBytes(), publicKey, cipher);
    System.out.println("Ciphertext: " + ciphertext);
    byte[] plaintext = rsaDecrypt(data.getBytes(), privateKey, cipher);
    System.out.println("Decrypted Plaintext: " + plaintext);
}

public static Cipher generateCipher() throws NoSuchAlgorithmException, NoSuchPaddingException {
    Cipher cipher = Cipher.getInstance("RSA");
    return cipher;
}

public static byte[] rsaEncrypt(byte[] data, Key publicKey, Cipher cipher) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {
      cipher.init(Cipher.ENCRYPT_MODE, publicKey);
      byte[] cipherData = cipher.doFinal(data);
      return cipherData;
}

public static byte[] rsaDecrypt(byte[] data, Key privateKey, Cipher cipher) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
      cipher.init(Cipher.DECRYPT_MODE, privateKey);
      byte[] cipherData = cipher.doFinal(data); // error line: at learning.RSA.rsaDecrypt(RSA.java:43)

      return cipherData;
}
}
