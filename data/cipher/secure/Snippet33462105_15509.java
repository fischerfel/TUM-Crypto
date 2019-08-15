        encryptionCipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
    byte[] ivbyte = encryptionCipher.getParameters().getParameterSpec(IvParameterSpec.class).getIV();

    encryptionCipher.init(Cipher.ENCRYPT_MODE, secret, new IvParameterSpec(ivbyte));

    decryptionCipher = Cipher.getInstance("AES/CBC/PKCS5Padding");

    decryptionCipher.init(Cipher.DECRYPT_MODE, secret, new IvParameterSpec(ivbyte));
