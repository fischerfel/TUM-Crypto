byte[] key = { '^', '(', 'S', '2', 'k', '*', '*','@', 'z', '/', 'a','#', '\\', 'd', 'R', 'G' };
byte[] dataToSend = new String("Hello Bob.").getBytes();
Cipher c;
String s = "";
try {
    c = Cipher.getInstance("AES/ECB/PKCS5Padding");
    SecretKeySpec k = new SecretKeySpec(key, "AES");
    c.init(Cipher.ENCRYPT_MODE, k);
    byte[] encryptedData = c.doFinal(dataToSend);

    s = new BASE64Encoder().encode(encryptedData);
}
