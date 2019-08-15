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

    // use CBC/NoPadding, with 0 IV -- (each message is creating it's own session key, so zero IV is ok)
    SecretKeySpec sessionKey = new SecretKeySpec(rawKey, "AES");
    Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
    cipher.init(Cipher.DECRYPT_MODE, sessionKey, new IvParameterSpec(new byte[16]));

    byte[] decryptedBlocks = cipher.doFinal(encryptedMessage);

    // check versus expected message
    byte[] expectedBytes = "Decryption Works -- using multiple blocks".getBytes();
    Assert.assertTrue("Incorrect Message" + new String(message), Arrays.equals(message, expectedBytes));
}
catch (Exception e) {
  e.printStackTrace();
}
