SecretKey secretAesKey ;
KeyGenerator keyGen = KeyGenerator.getInstance("AES");
keyGen.init(256);
secretAesKey = keyGen.generateKey();
if (secretAesKey != null) {
    Cipher aesCipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
    aesCipher.init(Cipher.ENCRYPT_MODE, secretAesKey);
    long aesEncryptStartTime = SystemClock.elapsedRealtime();
    CipherInputStream aesCis = new CipherInputStream(fis, aesCipher);
    int read;
    byte[] buffer = new byte[4096];
    while ((read = aesCis.read(buffer)) != -1) {
        aesFos.write(buffer, 0, read);
        aesFos.flush();
    }

    // Encrypt the generated key
    if (!encKeyFile.exists()) {
        encKeyFile.createNewFile();
    }

    try {
        byte[] encryptedAesKey = null;
        Cipher rsaCipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        rsaCipher.init(Cipher.ENCRYPT_MODE, readRsaPublicKeyFromResource(context));
        encryptedAesKey = rsaCipher.doFinal(secretAesKey.getEncoded());
        rsaFos.write(encryptedAesKey);
        rsaFos.flush();
    } catch (Exception e) {
        Log.e(LOG_TAG, "RSA encryption error", e);
    } finally {
        rsaFos.close();
    }
