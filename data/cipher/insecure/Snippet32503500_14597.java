public String encrypt(String message) throws Exception {
    String message1 = Base64.encodeToString(message.getBytes(), Base64.DEFAULT);
    String salt = "1abcdefgjijklmno";
    SecretKeySpec key = new SecretKeySpec(salt.getBytes(), "AES");
    Cipher c = Cipher.getInstance("AES");
    c.init(Cipher.ENCRYPT_MODE, key);
    byte[] encVal = c.doFinal(message1.getBytes());
    String encrypted = Base64.encodeToString(encVal, Base64.DEFAULT);
    return encrypted;
}

//Decryption
public String decrypt(String message) throws Exception {

    String salt = "1abcdefgjijklmno";
    Cipher c = Cipher.getInstance("AES");
    SecretKeySpec key = new SecretKeySpec(salt.getBytes(), "AES");
    c.init(Cipher.DECRYPT_MODE, key);
    byte[] decordedValue = Base64.decode(message.getBytes(), Base64.DEFAULT);
    byte[] decValue = c.doFinal(decordedValue);
    String decryptedValue = new String(decValue);
    String decoded = new String(Base64.decode(decryptedValue, Base64.DEFAULT));
    return decoded;
}
