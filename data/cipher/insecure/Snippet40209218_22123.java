Encrypt Function code:

boolean base64 = true;
key = "thisiskey";
plainText = "Google is an American multinational technology company specializing in Internet-related services";

SecretKeyFactory MyKeyFactory = SecretKeyFactory.getInstance("DES");
byte[] keyBytes = key.getBytes();

DESKeySpec generatedKeySpec = new DESKeySpec(keyBytes);
SecretKey generatedSecretKey = MyKeyFactory.generateSecret(generatedKeySpec);

Cipher generatedCipher = Cipher.getInstance("DES");
generatedCipher.init(Cipher.ENCRYPT_MODE, generatedSecretKey);

byte[] messsageStringBytes = plainText.getBytes();
byte[] encryptedMessage = generatedCipher.doFinal(messsageStringBytes);

String encryptedMessageString = new String(encryptedMessage);

if (base64) {
    encryptedMessageString = Base64.getEncoder().encodeToString(encryptedMessageString.getBytes("utf-8"));
}

return encryptedMessageString;
