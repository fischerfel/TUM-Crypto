public byte[] AESencrypt(String plainText, String encryptionKey) throws Exception {

    Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");

    SecretKeySpec key = new SecretKeySpec(encryptionKey.getBytes("UTF-8"), "AES");

    final byte[] iv = new byte[16];
    Arrays.fill(iv, (byte) 0x00);
    IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);

    cipher.init(Cipher.ENCRYPT_MODE, key,ivParameterSpec/*new IvParameterSpec(INITIALIZATIO_VECTOR.getBytes("UTF-8"))*/);

    return cipher.doFinal(plainText.getBytes("UTF-8"));

  }
