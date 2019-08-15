    public static String decrypt(String seed, String encrypted) throws Exception {
    byte[] rawKey = getRawKey(seed.getBytes());
    byte[] enc = toByte(encrypted);

    SecretKeySpec Spec = new SecretKeySpec(rawKey, "AES");
    Cipher cipher = Cipher.getInstance("AES");
    cipher.init(Cipher.DECRYPT_MODE, Spec);


    byte[] result = cipher.doFinal(enc);

    return new String(result);
}
