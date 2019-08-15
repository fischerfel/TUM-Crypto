//json encoding
JSONObject obj = new JSONObject();
obj.put("email", username);
obj.put("password", password);
obj.put("action", "login");

//function to encode base64
private String getBase64Encoded(String encryptedJsonString)
{
    byte[] encoded = Base64.encodeBase64(encryptedJsonString.getBytes());

    String encodedString = new String(encoded);

    return encodedString;
}

//function to encrypt in RC4
private String getRC4EncryptedString2(String string, String key) throws Exception
{
    Cipher cipher = Cipher.getInstance("RC4");
    SecretKeySpec rc4Key = new SecretKeySpec(key.getBytes(), "RC4");
    cipher.init(Cipher.ENCRYPT_MODE, rc4Key);

    byte[] cipherText = cipher.update(string.getBytes());

    return new String(cipherText);
}
