BufferedReader reader = new BufferedReader(new InputStreamReader(content.getInputStream()));
StringBuffer buffer = new StringBuffer();
String line = reader.readLine();
while (line != null) {
    buffer.append(line + "\n");
    line = reader.readLine();
}
reader.close();

BASE64Decoder decoder = new BASE64Decoder();
byte[] decodedBytes = decoder.decodeBuffer(buffer.toString());

SecretKeySpec blowfishKey = new SecretKeySpec(password, "Blowfish");
Cipher blowfishCipher = Cipher.getInstance("Blowfish");
blowfishCipher.init(Cipher.DECRYPT_MODE, blowfishKey);
byte[] decryptedBytes = blowfishCipher.doFinal(decodedBytes);
