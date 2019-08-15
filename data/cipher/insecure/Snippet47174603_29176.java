  public static String decryptIt(String value) {
    try {
        DESKeySpec keySpec = new DESKeySpec(new byte[]{105, 107, 18, 51, 114, 83, 51, 120, 121});//cryptoPass.getBytes("UTF8"));
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
        SecretKey key = keyFactory.generateSecret(keySpec);

        byte[] encrypedPwdBytes = Base64.decode(value, Base64.DEFAULT);
        // cipher is not thread safe
        Cipher cipher = Cipher.getInstance("DES");
        cipher.init(Cipher.DECRYPT_MODE, key);
        byte[] decrypedValueBytes = (cipher.doFinal(encrypedPwdBytes));

        // Log.d("aa", "Decrypted: " + value + " -> " + decrypedValue);
        return new String(decrypedValueBytes);
    } catch (Exception e) {
        e.printStackTrace();
    }
    return value;
}
