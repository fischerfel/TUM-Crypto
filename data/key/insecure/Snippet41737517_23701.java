public static void encryptFile() {
    File file = new File(Environment.getExternalStorageDirectory().getPath() + "/" + TARGET_FILE);

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
        fileOutputStream = new FileOutputStream(Environment.getExternalStorageDirectory().getPath() + "/" + ENCRYPT_FILE);

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec);

        //CipherOutputStream cipherOutputStream = new CipherOutputStream(fileOutputStream, cipher);

        int read;

        while ((read = fileInputStream.read(buffer)) > 0) {
            Log.i(TAG, "encrypt read= " + read);

            byte[] encryptedData = cipher.doFinal(buffer);
            if (encryptedData != null) {
                Log.i(TAG, "encrypted size= " + encryptedData.length);
                fileOutputStream.write(encryptedData, 0, read);
            }

            //cipherOutputStream.write(buffer, 0, buffer.length);
        }
        //cipherOutputStream.flush();
        //cipherOutputStream.close();
        fileInputStream.close();
        fileOutputStream.close();

    } catch (IOException | NoSuchAlgorithmException | NoSuchPaddingException
            | IllegalBlockSizeException | BadPaddingException
            | InvalidAlgorithmParameterException | InvalidKeyException e) {
        e.printStackTrace();
    }
}
