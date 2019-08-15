private static SecretKeySpec createKeyFromString(String plainKey) {

    MessageDigest sha = null;
    byte[] key;
    SecretKeySpec secretKey = null;

    try {

        key = plainKey.getBytes("UTF-8");
        sha = MessageDigest.getInstance("SHA-1");
        key = sha.digest(key);
        key = Arrays.copyOf(key, 16);

        secretKey = new SecretKeySpec(key, "AES");

    } catch(NoSuchAlgorithmException e) {
        e.printStackTrace();
    } catch(UnsupportedEncodingException e) {
        e.printStackTrace();
    }

    return secretKey;

}

private static String AESCrypt(String password, SecretKeySpec secretKey) {
    try {

        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        return Base64.getEncoder().encodeToString(cipher.doFinal(password.getBytes("UTF-8")));

    } catch (Exception e) {
        System.out.println(e.toString());
    }
    return null;
}


public static void main(String[] args) {

    System.out.println(Pay.AESCrypt("password", Pay.createKeyFromString("key")));

}
