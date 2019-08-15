public class NewClass {
    public static void main(String [] args) {
        try {
            String plainText = "Hello World!!!!";
            String encryptionKey = "E072EDF9534053A0B6C581C58FBF25CC";

            System.out.println("Before encryption - " + plainText);

            byte[] cipherText = encrypt(plainText, encryptionKey);

            System.out.println("After encryption - " + cipherText);

            String decrypted = decrypt(cipherText, encryptionKey);

            // -> Hello World!!!!
            System.out.println("After decryption - " + decrypted);
        } catch (Exception e) {
            e.printStackTrace();
        } 
    }

    public static byte[] encrypt(String plainText, String passkey) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding", "SunJCE");
        SecretKeySpec key = new SecretKeySpec(hexStringToByteArray(passkey), "AES");
        cipher.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(new byte[cipher.getBlockSize()]));
        return cipher.doFinal(plainText.getBytes());
    }

    public static String decrypt(byte[] cipherText, String passkey) throws Exception{
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding", "SunJCE");
        SecretKeySpec key = new SecretKeySpec(hexStringToByteArray(passkey), "AES");
        cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(new    byte[cipher.getBlockSize()]));
        return new String(cipher.doFinal(cipherText));
} 
