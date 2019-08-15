private SecretKeySpec secretKey;
private IvParameterSpec ivSpec;

public void setKey(String myKey) {
    MessageDigest sha = null;
    try {
        byte[] key = myKey.getBytes("UTF-8");
        sha = MessageDigest.getInstance("SHA-1");
        key = sha.digest(key);
        key = Arrays.copyOf(key, 16);
        secretKey = new SecretKeySpec(key, "AES");

        byte[] iv = new String("1010101010101010").getBytes("UTF-8");
        ivSpec = new IvParameterSpec(iv);

    } catch (NoSuchAlgorithmException e) {
        e.printStackTrace();
    } catch (UnsupportedEncodingException e) {
        e.printStackTrace();
    }
}

public String encrypt(String strToEncrypt) {
    try {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivSpec);
        return Base64.encode(cipher.doFinal(strToEncrypt.getBytes("UTF-8")));
    } catch (Exception e) {
        e.printStackTrace();
    }
    return null;
}

public String decrypt(String strToDecrypt) {
    try {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, secretKey, ivSpec);
        return new String(cipher.doFinal(Base64.decode(strToDecrypt)));
    } catch (Exception e) {
        e.printStackTrace();
    }
    return null;
}

public static void main(String[] args) {

    AESText aes = new AESText();
    final String secretKey = "com.secure.test.projectjasdS/FjkGkGhkGjhG786Vjfg=tjGFGH";
    aes.setKey(secretKey);

    String originalString = "test set se ts et set s et se";
    String encryptedString = aes.encrypt(originalString);
    String decryptedString = aes.decrypt(encryptedString);

    System.out.println("origin: " + originalString);
    System.out.println("encrypted: " + encryptedString);
    System.out.println("decrypted: " + decryptedString);
}
