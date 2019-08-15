private static SecretKey generateKeyAndroid(String inputKey) throws GeneralSecurityException {
    // Number of PBKDF2 hardening rounds to use.
    final int iterations = 50000;
    // Generate a 256-bit key
    final int outputKeyLength = 256;

    inputKey = Uri.decode(inputKey);

    byte[] keySalt = new byte[32]; // this will actually be filled

    PBEKeySpec keySpec = new PBEKeySpec(inputKey.toCharArray(), keySalt, iterations, outputKeyLength);
    SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("PBEWITHSHA1AND256BITAES-CBC-BC");
    SecretKey secretKey = keyFactory.generateSecret(keySpec);

    return secretKey;
}
