private void decrypt_AES_CBC_PKCS7(final byte[] symKeyBytes, final FileInputStream inputStream, final FileOutputStream outputStream) throws Exception {

    Security.addProvider(new BouncyCastleProvider());

    // Read init vector
    final byte[] iv = new byte[16];
    inputStream.read(iv, 0, 16);

    // Prepare for decryption
    final SecretKeySpec secretKeySpec = new SecretKeySpec(symKeyBytes, "AES");
    final Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding", new BouncyCastleProvider());
    cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, new IvParameterSpec(iv));

    // Decrypt chunk by chunk
    int chunkLen = 0;
    final byte[] buffer = new byte[CHUNK_SIZE_DECRTPY]; // CHUNK_SIZE_DECRTPY = 20 * 1024 * 1024;

    while ((chunkLen = inputStream.read(buffer)) > 0) {

        byte[] decrypted = cipher.doFinal(buffer, 0, chunkLen);

        outputStream.write(decrypted, 0, decrypted.length);
    }

    // close streams
    inputStream.close();
    outputStream.close();
}
