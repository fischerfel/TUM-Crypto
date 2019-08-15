private static void generatePrivateKey(String pwd) {

    try {
        PBEKeySpec keySpec = new PBEKeySpec(pwd.toCharArray());
        SecretKeyFactory kf = SecretKeyFactory.getInstance("PBEWithHmacSHA256AndAES_128");
        PRIVATE_KEY = kf.generateSecret(keySpec);
    } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
        e.printStackTrace();
    }
}
