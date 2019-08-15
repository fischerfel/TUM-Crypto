public static String decrypt(byte[] input) throws Exception {
    Cipher cipher = Cipher.getInstance("AES/ECB/NoPadding");
    byte[] key = getKey(input[1]);
    SecretKey secretKey = new SecretKeySpec(key, 0, key.length, "AES/ECB/NoPadding");
    cipher.init(Cipher.DECRYPT_MODE, secretKey, new IvParameterSpec(getIV()));
    // remove first 4 since C# code reads past those
    byte[] finalDecoded = Arrays.copyOfRange(input, 4, input.length);
    byte[] decryptedVal = cipher.doFinal(finalDecoded);
    return new String(decryptedVal);
}
