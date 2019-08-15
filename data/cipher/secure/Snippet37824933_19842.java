Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPPadding");
OAEPParameterSpec oaepParams = new OAEPParameterSpec("SHA-256", "MGF1", new MGF1ParameterSpec("SHA-1"), PSource.PSpecified.DEFAULT);
cipher.init(Cipher.DECRYPT_MODE, privateKey, oaepParams);
byte[] decryptedAESKeyOfServer = null;
decryptedAESKeyOfServer = cipher.doFinal(EncryptedClientAES);
