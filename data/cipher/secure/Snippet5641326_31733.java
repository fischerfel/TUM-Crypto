public String dec(String password, String salt, String encString) throws Throwable {
    // AES algorithm with CBC cipher and PKCS5 padding
    Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding", "BC");

    // Construct AES key from salt and 50 iterations 
    PBEKeySpec pbeEKeySpec = new PBEKeySpec(password.toCharArray(), toByte(salt), 50, 256);
    SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("PBEWithSHA256And256BitAES-CBC-BC");
    SecretKeySpec secretKey = new SecretKeySpec(keyFactory.generateSecret(pbeEKeySpec).getEncoded(), "AES");

    // IV seed for first block taken from first 32 bytes
    byte[] ivData = toByte(encString.substring(0, 32));
    // AES encrypted data
    byte[] encData = toByte(encString.substring(32));

    cipher.init( Cipher.DECRYPT_MODE, secretKey, new IvParameterSpec( ivData ) );

    return new String( cipher.doFinal( encData ) );
}
