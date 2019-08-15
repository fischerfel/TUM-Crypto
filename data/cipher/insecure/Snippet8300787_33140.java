public class EncryptionTest {
    public static void main(String[] args) throws Exception {
        byte[] keyBytes = "1234567890azertyuiopqsdf".getBytes("ASCII");
        DESedeKeySpec keySpec = new DESedeKeySpec(keyBytes);
        SecretKeyFactory factory = SecretKeyFactory.getInstance("DESede");
        SecretKey key = factory.generateSecret(keySpec);
        byte[] text = "Hello world".getBytes("ASCII");

        Cipher cipher = Cipher.getInstance("DESede");
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] encrypted = cipher.doFinal(text);

        cipher = Cipher.getInstance("DESede");
        cipher.init(Cipher.DECRYPT_MODE, key);
        byte[] decrypted = cipher.doFinal(encrypted);
        System.out.println(new String(decrypted, "ASCII"));
    }
}
