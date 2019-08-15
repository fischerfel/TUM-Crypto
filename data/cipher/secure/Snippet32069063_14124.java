public static void main(String[] args) {

    try {
        int randomNumber = CNStationQueueUtil.randInt(0, 99999);
        String key = "AES_KEY_TAKENUMB";
        byte[] bytes = EncryptHelper.encrypt(key, String.format("%%%d%%%d", 1001, randomNumber));
        String str = new String(bytes, "UTF8");
        System.out.println("str = " + str);
        System.out.println();
        byte[] utf8Bytes = str.getBytes("UTF8");
        printBytes(utf8Bytes, "utf8Bytes");

    } catch (Exception e) {
        e.printStackTrace();
    }

}



public class EncryptHelper {

    public static byte[] encrypt(String key, String value)
            throws GeneralSecurityException {

        byte[] raw = key.getBytes(Charset.forName("UTF-8"));
        if (raw.length != 16) {
            throw new IllegalArgumentException("Invalid key size.");
        }

        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec,
                new IvParameterSpec(new byte[16]));
        return cipher.doFinal(value.getBytes(Charset.forName("UTF-8")));
    }

    public static String decrypt(String key, byte[] encrypted)
            throws GeneralSecurityException {

        byte[] raw = key.getBytes(Charset.forName("UTF-8"));
        if (raw.length != 16) {
            throw new IllegalArgumentException("Invalid key size.");
        }
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, skeySpec,
                new IvParameterSpec(new byte[16]));
        byte[] original = cipher.doFinal(encrypted);

        return new String(original, Charset.forName("UTF-8"));
    }
}
