try {
    KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(
            KeyProperties.KEY_ALGORITHM_RSA, "AndroidKeyStore");
    keyPairGenerator.initialize(
            new KeyGenParameterSpec.Builder(
                    "key1",
                    KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT)
                    .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_RSA_PKCS1)
                    .build());
    KeyPair keyPair = keyPairGenerator.generateKeyPair();

    // error in android 6: InvalidKeyException: Need RSA private or public key AndroidOpenSSL
    // error in android 5: NoSuchProviderException: Provider not available: AndroidKeyStoreBCWorkaround
    String provider = Build.VERSION.SDK_INT < Build.VERSION_CODES.M ? "AndroidOpenSSL" : "AndroidKeyStoreBCWorkaround";

    Cipher inCipher = Cipher.getInstance("RSA/ECB/PKCS1Padding", provider);
    inCipher.init(Cipher.ENCRYPT_MODE, keyPair.getPublic());

    Cipher outCipher = Cipher.getInstance("RSA/ECB/PKCS1Padding", provider);
    outCipher.init(Cipher.DECRYPT_MODE, keyPair.getPrivate());

    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    CipherOutputStream cipherOutputStream = new CipherOutputStream(
            outputStream, inCipher);

    String plainText = "This is a text";

    cipherOutputStream.write(plainText.getBytes("UTF-8"));
    cipherOutputStream.close();

    String ecryptedText = Base64.encodeToString(outputStream.toByteArray(), Base64.DEFAULT);
    Log.d(TAG, "Encrypt = " + ecryptedText);

    String cipherText = ecryptedText;
    CipherInputStream cipherInputStream = new CipherInputStream(
            new ByteArrayInputStream(Base64.decode(cipherText, Base64.DEFAULT)), outCipher);

    ArrayList<Byte> values = new ArrayList<>();
    int nextByte;
    while ((nextByte = cipherInputStream.read()) != -1) {
        values.add((byte)nextByte);
    }

    byte[] bytes = new byte[values.size()];
    for(int i = 0; i < bytes.length; i++) {
        bytes[i] = values.get(i).byteValue();
    }

    String finalText = new String(bytes, 0, bytes.length, "UTF-8");
    Log.d(TAG, "Decrypt = " + finalText);
} catch (javax.crypto.NoSuchPaddingException e) {
    Log.e(TAG, Log.getStackTraceString(e));
} catch (IOException e) {
    Log.e(TAG, Log.getStackTraceString(e));
} catch (NoSuchAlgorithmException e) {
    Log.e(TAG, Log.getStackTraceString(e));
} catch (NoSuchProviderException e) {
    Log.e(TAG, Log.getStackTraceString(e));
} catch (InvalidAlgorithmParameterException e) {
    Log.e(TAG, Log.getStackTraceString(e));
} catch (InvalidKeyException e) {
    Log.e(TAG, Log.getStackTraceString(e));
} catch (UnsupportedOperationException e) {
    Log.e(TAG, Log.getStackTraceString(e));
}
