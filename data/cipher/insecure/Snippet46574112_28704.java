public class Blowfish {
    static SecretKey key = null;
    static String IV = "AAAAAAAA";

    public Blowfish(int bits, InetAddress ip) throws UnknownHostException, IOException, NoSuchAlgorithmException {

        KeyGenerator keyGenerator = KeyGenerator.getInstance("Blowfish");
        keyGenerator.init(bits); // 32 a 448 (default 128)
        key = keyGenerator.generateKey();
        String encodedKey = Base64.getEncoder().encodeToString(key.getEncoded());
        RSA.exchangeKey(encodedKey, ip);

    }

    public static byte[] encrypt(byte[] plainData, int offset, int length) throws Exception {
        Cipher cipher = Cipher.getInstance("Blowfish/OFB/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(IV.getBytes("UTF-8")));
        return cipher.doFinal(plainData, offset, length);

    }

    public static byte[] decrypt(byte[] cipherSound, int offset, int length) throws Exception {
        Cipher cipher = Cipher.getInstance("Blowfish/OFB/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(IV.getBytes("UTF-8")));
        return cipher.doFinal(cipherSound, offset, length);
    }

}
