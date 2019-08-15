 private SecretKey getKey(String user_id, String salt) throws NoSuchAlgorithmException,
        InvalidKeySpecException, UnsupportedEncodingException {
    SecretKey secretKey = null;

    String PBE_SHA256_256BitAES_CBC_BC                  = "PBEWithSHA256And256BitAES-CBC-BC";
    String AES_ENCRYPTION                               = "AES";

    SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance(PBE_SHA256_256BitAES_CBC_BC);
    KeySpec keySpec = new PBEKeySpec(user_id.toCharArray(), salt.getBytes(), Constants.ITERATION_COUNT, Constants.KEY_LENGTH);

    secretKey = secretKeyFactory.generateSecret(keySpec);
    secretKey = new SecretKeySpec(secretKey.getEncoded(), AES_ENCRYPTION );

    return  secretKey;
}
