private static byte[] encodeFile(byte[] yourKey, byte[] fileData)
        throws Exception {
    byte[] encrypted = null;
    SecretKeySpec skeySpec = new SecretKeySpec(yourKey, 0, yourKey.length, "AES");
    Cipher cipher = Cipher.getInstance("AES/ECB/PKCS7PADDING");
    cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
    encrypted = cipher.doFinal(fileData);
    return encrypted;
}

private static byte[] generateKey() throws NoSuchAlgorithmException {
    byte[] keyStart = "This is my key".getBytes();
    String id = "dummypass";
    int iterationCount = 1000;
    int saltLength = 32;
    int keyLength = 128;
    SecureRandom random = new SecureRandom();
    byte[] salt = Arrays.copyOf(keyStart,saltLength);
    random.nextBytes(salt);
    KeySpec keySpec = new PBEKeySpec(id.toCharArray(), salt,
            iterationCount, keyLength);
    SecretKeyFactory keyFactory = SecretKeyFactory
            .getInstance("PBKDF2WithHmacSHA1");
    byte[] keyBytes = new byte[0];
    try {
        keyBytes = keyFactory.generateSecret(keySpec).getEncoded();
    } catch (InvalidKeySpecException e) {
        e.printStackTrace();
    }
    SecretKey key = new SecretKeySpec(keyBytes, "AES");
    return key.getEncoded();
}

private static byte[] decodeFile(byte[] yourKey, byte[] encryptedData)
        throws Exception {
    SecretKeySpec skeySpec = new SecretKeySpec(yourKey, 0, yourKey.length,
            "AES");
    Cipher cipher = Cipher.getInstance("AES/ECB/PKCS7PADDING");
    cipher.init(Cipher.DECRYPT_MODE, skeySpec);
    byte[] decrypted = cipher.doFinal(encryptedData);
    return decrypted;
}

public static void Encrypt(byte[] bytesToEncrypt, File target) {
    try {
        BufferedOutputStream bos = new BufferedOutputStream(
                new FileOutputStream(target));
        byte[] key = generateKey();
        byte[] encryptedBytes = encodeFile(key, bytesToEncrypt);
        bos.write(encryptedBytes);
        bos.flush();
        bos.close();
    } catch (FileNotFoundException e) {
        e.printStackTrace();
    } catch (IOException e) {
        e.printStackTrace();
    } catch (Exception e) {
        e.printStackTrace();
    }
}
public static  byte[] DecryptFile(byte[] bytesToDecrypt) {
    byte[] decodedData = new byte[0];
    try {
        byte[] key = generateKey();
        decodedData = decodeFile(key, bytesToDecrypt);
    } catch (Exception e) {
        e.printStackTrace();
    }
    return decodedData;
}
