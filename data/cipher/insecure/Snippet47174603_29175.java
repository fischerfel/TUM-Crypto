public static String encryptIt(String value) {
try {
    DESKeySpec keySpec = new DESKeySpec(new byte[]{105, 107, 18, 51, 114, 83, 51, 120, 121}); 
    SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
    SecretKey key = keyFactory.generateSecret(keySpec);

    byte[] clearText = value.getBytes("UTF8");
    // Cipher is not thread safe
    Cipher cipher = Cipher.getInstance("DES");
    cipher.init(Cipher.ENCRYPT_MODE, key);

    //   Log.d("aa", "Encrypted: " + value + " -> " + encrypedValue);
    return Base64.encodeToString(cipher.doFinal(clearText), Base64.DEFAULT);
} catch (Exception e) {
    e.printStackTrace();
}
return value;
