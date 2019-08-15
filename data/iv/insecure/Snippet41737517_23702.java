public static void decryptFile() {
    File file = new File(Environment.getExternalStorageDirectory().getPath() + "/" + ENCRYPT_FILE);

    FileInputStream fileInputStream;
    FileOutputStream fileOutputStream;

    byte[] buffer = new byte[1024 * 8];

    IvParameterSpec ivParameterSpec = new IvParameterSpec("1234567890123456".getBytes());

    byte[] key = "only for testing".getBytes();
    MessageDigest sha;
    try {
        sha = MessageDigest.getInstance("SHA-1");
        key = sha.digest(key);
    } catch (NoSuchAlgorithmException e) {
        e.printStackTrace();
    }
    key = Arrays.copyOf(key, 16); // use only first 128 bit

    SecretKeySpec secretKeySpec = new SecretKeySpec(key, "AES");

    try {
        fileInputStream = new FileInputStream(file);
        fileOutputStream = new FileOutputStream(Environment.getExternalStorageDirectory().getPath() + "/" + DECRYPT_FILE);

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivParameterSpec);

        //CipherInputStream cipherInputStream = new CipherInputStream(fileInputStream, cipher);

        int read;

        while ((read = fileInputStream.read(buffer)) > 0) {
            Log.i(TAG, "decrypt read= " + read);

            byte[] decryptedData = cipher.doFinal(buffer);
            if (decryptedData != null) {
                Log.i(TAG, "decrypted size= " + decryptedData.length);
                fileOutputStream.write(decryptedData, 0, read);
            }

            //fileOutputStream.write(buffer, 0, buffer.length);
        }
        fileOutputStream.flush();
        fileOutputStream.close();
        //cipherInputStream.close();
        fileInputStream.close();

    } catch (IOException | NoSuchAlgorithmException | NoSuchPaddingException
            | IllegalBlockSizeException | BadPaddingException
            | InvalidAlgorithmParameterException | InvalidKeyException e) {
        e.printStackTrace();
    }
}
