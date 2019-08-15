public static String encrypt(String encodeKey, String inputFile) throws Exception {

    byte[] input = getStringFromFile(inputFile).toString().getBytes("utf-8");

    MessageDigest md = MessageDigest.getInstance("MD5");
    byte[] thedigest = md.digest(encodeKey.getBytes("UTF-8"));
    SecretKeySpec skc = new SecretKeySpec(thedigest, "AES/ECB/PKCS5Padding");
    Cipher cipher = Cipher.getInstance("AES");
    cipher.init(Cipher.ENCRYPT_MODE, skc);

    byte[] cipherText = new byte[cipher.getOutputSize(input.length)];
    int ctLength = cipher.update(input, 0, input.length, cipherText, 0);
    String data = Base64.encodeToString(cipherText, Base64.DEFAULT);

    Log.d("Crypto | Length", String.valueOf(ctLength));
    Log.d("Crypto | Keypass", encodeKey);

    return data;
}
