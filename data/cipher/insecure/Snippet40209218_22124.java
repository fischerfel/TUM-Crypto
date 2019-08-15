boolean dbase64 = true;
dkey = "thisiskey";
messageToDecrypt = "mñqè•ÀPŒ�øf\"ß¦\±õ¤ù'È9¢ëyT ÍQEÁ|;ëâÉ÷JWú"; // Message from above code

SecretKeyFactory MyKeyFactory = SecretKeyFactory.getInstance("DES");
byte[] dkeyBytes = dkey.getBytes();

DESKeySpec generatedKeySpec = new DESKeySpec(dkeyBytes);
SecretKey generatedSecretKey = MyKeyFactory.generateSecret(generatedKeySpec);

Cipher generatedCipher = Cipher.getInstance("DES");
generatedCipher.init(Cipher.DECRYPT_MODE, generatedSecretKey);

if (dbase64) {
    byte[] decodedBytes = Base64.getDecoder().decode(dencryptedText);
    dencryptedText = new String(decodedBytes, "utf-8");
}

byte[] messsageStringBytes = dencryptedText.getBytes();
byte[] encryptedMessage = generatedCipher.doFinal(messsageStringBytes);

String decryptedMessageString = new String(encryptedMessage);

return decryptedMessageString;
