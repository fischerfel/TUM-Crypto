public static String decrypt(String input, String key) {

    byte[] output = null;
    String newString = "";

    try {

        SecretKeySpec skey = new SecretKeySpec(key.getBytes(), "AES");
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, skey);
        output = cipher.doFinal(Base64.decode(input, Base64.DEFAULT));
        newString = new String(output);

    } catch(Exception e) {}

    return newString;
}
