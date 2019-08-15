try
{
    FileInputStream in = new FileInputStream("test.aes");
    DataInputStream dataIn = new DataInputStream(in);

    // stream key and message
    byte[] rawKey = new byte[16];
    dataIn.read(rawKey);
    byte encryptedMessageLen = dataIn.readByte();
    byte[] encryptedMessage = new byte[encryptedMessageLen];
    dataIn.read(encryptedMessage);

    // use CBC/PKCS5PADDING, with 0 IV -- default for Microsoft Base Cryptographic Provider
    SecretKeySpec sessionKey = new SecretKeySpec(rawKey, "AES");
    Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
    cipher.init(Cipher.DECRYPT_MODE, sessionKey, new IvParameterSpec(new byte[16]));

    cipher.doFinal(encryptedMessage);
}
catch (Exception e) {
  e.printStackTrace();
}
