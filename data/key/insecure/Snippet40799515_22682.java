public class EncriptionDecriptionUtils {
    private static byte[] nrlmEncription = { 0x77, 0x71, 0x72, 0x76, 0x52,
     0x76, 0x44, 0x56, 0x68, 0x66, 0x75, 0x68, 0x77, 0x4b, 0x6f, 0x7d };

    public String decriptionOfData(String data) throws Exception {
        SecretKeySpec skeySpec = new SecretKeySpec(nrlmEncription, "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, skeySpec);
        byte[] decrypted = cipher.doFinal(Base64.decode(data,         Base64.DEFAULT));
        return new String(decrypted);
    }
    public String encriptionOfData(String data) throws Exception {
       Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
       final SecretKeySpec secretKey = new SecretKeySpec(nrlmEncription, "AES");
       cipher.init(Cipher.ENCRYPT_MODE, secretKey);
       return Base64.encodeToString(cipher.doFinal(data.getBytes()), Base64.DEFAULT);
    }
}
