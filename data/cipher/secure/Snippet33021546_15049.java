public static String encrypt(byte[] keyValue, String plaintext) throws Exception {
    Key key = new SecretKeySpec(keyValue, "AES");
    //serialize
    String serializedPlaintext = "s:" + plaintext.length() + ":\"" + plaintext + "\";";
    byte[] plaintextBytes = serializedPlaintext.getBytes("UTF-8");

    Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding");
    c.init(Cipher.ENCRYPT_MODE, key);
    byte[] iv = c.getIV();
    byte[] encVal = c.doFinal(plaintextBytes);
    String encryptedData = Base64.encodeToString(encVal, Base64.NO_WRAP);

    SecretKeySpec macKey = new SecretKeySpec(keyValue, "HmacSHA256");
    Mac hmacSha256 = Mac.getInstance("HmacSHA256");
    hmacSha256.init(macKey);
    hmacSha256.update(Base64.encode(iv, Base64.NO_WRAP));
    byte[] calcMac = hmacSha256.doFinal(encryptedData.getBytes("UTF-8"));
    String mac = new String(Hex.encodeHex(calcMac));
    //Log.d("MAC",mac);

    AesEncryptedData aesData = new AesEncryptedData(
            Base64.encodeToString(iv, Base64.NO_WRAP),
            encryptedData,
            mac);

    String aesDataJson = new Gson().toJson(aesData);

    return Base64.encodeToString(aesDataJson.getBytes("UTF-8"), Base64.DEFAULT);
}
