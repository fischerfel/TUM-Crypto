public SecretKey generateKey(String password, String salt) {
    char[] passChars =   password.toCharArray();
    byte[] saltBytes =   salt.getBytes();
    SecretKeyFactory keyFactory =   SecretKeyFactory.getInstance("PBEWithHmacSHA256AndAES_128");
    PBEKeySpec keySpec  =   new PBEKeySpec(passChars, saltBytes, 2048, 128);
    SecretKey secretKey =   keyFactory.generateSecret(keySpec);
    byte[] encodedKey =   secretKey.getEncoded();
    System.out.println("key: " + new String(encodedKey));

    return new SecretKeySpec(encodedKey, "AES"); 
}
