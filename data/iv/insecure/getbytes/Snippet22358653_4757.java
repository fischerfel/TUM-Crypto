public class AES {
    public static void main(String[] args) throws Exception {
        byte[] data = "abcdefghijkl".getBytes();
        byte[] tmp = null;
        SecretKey tempKey1 = generateKey();
        // saveKey(tempKey1);
        // SecretKey tempKey2 = loadKey();
        tmp = encrypt(data, tempKey1);
        byte[] printString = decrypt(tmp, tempKey1);
        System.out.println(Arrays.equals(data, printString));

    }

    public static SecretKey generateKey() throws NoSuchAlgorithmException {
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        keyGenerator.init(128);
        SecretKey key = keyGenerator.generateKey();
        return key;
    }

    public static byte[] encrypt(byte[] data, SecretKey encryptionKey) throws Exception {
        IvParameterSpec iv = new IvParameterSpec("0102030405060708".getBytes());
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, encryptionKey, iv);
        byte[] encryptData = cipher.doFinal(data);

        return encryptData;
    }

    public static byte[] decrypt(byte[] tmp, SecretKey encryptionKey) throws Exception {
        IvParameterSpec iv = new IvParameterSpec("0102030405060708".getBytes());
        SecretKeySpec spec = new SecretKeySpec(encryptionKey.getEncoded(), "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, spec, iv);

        System.out.println(tmp.length);
        return cipher.doFinal(tmp);

    }
}
