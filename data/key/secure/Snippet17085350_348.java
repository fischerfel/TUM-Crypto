public SecretKey getSymmetricKey(String keyString) {
    SecretKey secretKey = null;
    try {
        byte[] raw = Base64.decode(keyString);
        secretKey = new SecretKeySpec(raw, "AES");
        System.out.println("key "+secretKey);
    } catch (Exception e) {
        e.printStackTrace();
    }
    return secretKey;
}
