public class AesCrypter {
    static String IV = "AAAAAAAAAAAAAAAA";
    static String aesKey = "0123456789abcdef";

    public static byte[] encrypt(String unecryptedText) throws Exception {
        Cipher encrypt = Cipher.getInstance("AES/CBC/PKCS5Padding", "SunJCE");
        SecretKeySpec key = new SecretKeySpec(aesKey.getBytes("UTF-8"), "AES");
        encrypt.init(Cipher.ENCRYPT_MODE, key,new IvParameterSpec(IV.getBytes("UTF-8")));
        return encrypt.doFinal(unecryptedText.getBytes("UTF-8"));
    }

    public static String decrypt(String cryptedText) throws Exception{
        byte[] bytes = cryptedText.getBytes(StandardCharsets.UTF_8);
        Cipher decrypt = Cipher.getInstance("AES/CBC/PKCS5Padding", "SunJCE");
        SecretKeySpec key = new SecretKeySpec(aesKey.getBytes("UTF-8"), "AES");
        decrypt.init(Cipher.DECRYPT_MODE, key,new IvParameterSpec(IV.getBytes("UTF-8")));
        return new String(decrypt.doFinal(bytes),"UTF-8"); // this line
    }
}
