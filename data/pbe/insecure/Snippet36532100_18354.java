public String encryption(String clearText) throws Exception {
    SecretKeyFactory factory = SecretKeyFactory
            .getInstance("PBKDF2WithHmacSHA1");
    PBEKeySpec pbeKeySpec = new PBEKeySpec(clearText.toCharArray(),
            new byte[] { 0x49, 0x76, 0x61, 0x6e, 0x20, 0x4d, 0x65, 0x64,
                    0x76, 0x65, 0x64, 0x65, 0x76 }, 1000, 384);
    Key secretKey = factory.generateSecret(pbeKeySpec);
    byte[] key = new byte[32];
    byte[] iv = new byte[16];
    System.arraycopy(secretKey.getEncoded(), 0, key, 0, 32);
    System.arraycopy(secretKey.getEncoded(), 32, iv, 0, 16);

    SecretKeySpec secret = new SecretKeySpec(key, "AES");
    String result = new String(Base64.encodeBytes(secret.getEncoded()));
    System.out.println("Result: " + result);
    return result;
}
