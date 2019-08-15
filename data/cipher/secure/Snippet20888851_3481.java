public class TestAES {

public static void main(String[] args) {

    try {
        byte[] salt = new byte[8];
        new SecureRandom().nextBytes(salt);

        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        KeySpec keySpec = new PBEKeySpec("myPassword".toCharArray(), salt, 100, 128);

        SecretKey tmp = keyFactory.generateSecret(keySpec);
        SecretKeySpec key = new SecretKeySpec(tmp.getEncoded(), "AES");

        Cipher enCipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        enCipher.init(Cipher.ENCRYPT_MODE, key);

        // enCipher.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(iv));

        byte[] cipherBytes = enCipher.doFinal("myMessage".getBytes());
        String cipherMsg = BaseEncoding.base64().encode(cipherBytes);

        System.out.println("Encrypted message: " + cipherMsg);

    } catch (Exception ex) {
        ex.printStackTrace();
    }

}
}
