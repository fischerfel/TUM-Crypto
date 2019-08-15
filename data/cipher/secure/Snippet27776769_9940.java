public void testRSA() throws InvalidKeySpecException, NoSuchAlgorithmException, IOException, IllegalBlockSizeException, InvalidKeyException, BadPaddingException, NoSuchPaddingException {
    String rawPublicKey = new String(StreamUtils.loadBytes(getContext().getAssets().open("public.key")));
    Log.d(TAG, rawPublicKey);
    rawPublicKey = rawPublicKey.replace("-----BEGIN PUBLIC KEY-----\n", "");
    rawPublicKey = rawPublicKey.replace("-----END PUBLIC KEY-----", "");
    byte[] encoded = Base64.decode(rawPublicKey, 0);
    PublicKey publicKey = SignUtils.getAsPublicKey(encoded);

    byte[] encryptedFile = StreamUtils.loadBytes(getContext().getAssets().open("encrypted_file.bin"));
    String encryptedFileString = new String(encryptedFile);

    //STACK_NOTE: This file contains 3 parts but only first is used by this code.
    // encryptedFileParts[0] is equal to test_file from OpenSSL test
    String[] encryptedFileParts = encryptedFileString.split(";");
    Log.d(TAG, new String(encryptedFileParts[0].getBytes()));

    byte[] encryptedKey = Base64.decode(encryptedFileParts[0].getBytes(), 0);

    Log.d(TAG, new String(encryptedKey));

    Cipher cipher = Cipher.getInstance("RSA");
    cipher.init(Cipher.DECRYPT_MODE, publicKey);
    byte[] bytesPassword = cipher.doFinal(encryptedKey);

    String passwd = new String(bytesPassword, "UTF8");
    Log.d(TAG, "Recieved password = " + passwd);
    assertEquals("@rm@gEdon2014", passwd);
}
