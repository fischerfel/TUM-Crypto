public static String generateSalt() {
    SecureRandom random = new SecureRandom();
    String salt = new BigInteger(130, random).toString(32);
    return salt;
}
public static String hashKey(String key, String salt) throws UnsupportedEncodingException, NoSuchAlgorithmException {
    String combinedKey = key + salt;
    MessageDigest digest = MessageDigest.getInstance("SHA-256");
    digest.update(combinedKey.getBytes("UTF-8"));
    byte[] hash = digest.digest();
    String hashedKey = String.format("%032X", new BigInteger(+1, hash));
    return hashedKey;
}
private static SecretKeySpec getSecretKey(String myKey) {
    SecretKeySpec secretKey = null;
    byte[] key;
    MessageDigest sha = null;
    try {
        key = myKey.getBytes("UTF-8");
        sha = MessageDigest.getInstance("SHA-1");
        key = sha.digest(key);
        System.out.println(Base64.encode(key));
        key = Arrays.copyOf(key, 16);
        String a = Base64.encode(key);
        System.out.println("key:"+a);
        secretKey = new SecretKeySpec(key, "AES");

    } catch (NoSuchAlgorithmException e) {
        e.printStackTrace();
    } catch (UnsupportedEncodingException e) {
        e.printStackTrace();
    }

    return secretKey;
}
public static String encrypt(String stringToEncrypt, String secret) {
    String encodedEncryptedData = null;

    try {
        SecretKeySpec secretKey = getSecretKey(secret);
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] encryptedData = cipher.doFinal(stringToEncrypt.getBytes("UTF-8"));
        //System.out.println(encryptedData);
        encodedEncryptedData = Base64.encode(encryptedData);
    } catch (Exception e) {
        System.out.println("Error while encrypting: " + e.toString());
    }
    return encodedEncryptedData;
}

public static void main(String[] args) throws NoSuchAlgorithmException, UnsupportedEncodingException {
    String salt = generateSalt();
    String hash = hashKey("FEAD286FF2678F57C7865B6D6935C0C3",salt);
    //System.out.println(hash);
    String message = "60;50;28042017080701";
    String encryptedData = encrypt(message,hash);
    System.out.println(encryptedData);
}
