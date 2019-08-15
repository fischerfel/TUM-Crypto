    SecretKeySpec localSecretKeySpec = new SecretKeySpec("aaaaaaaaaaaaaaaa".getBytes(), "AES");
    Cipher localCipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
    localCipher.init(Cipher.ENCRYPT_MODE, localSecretKeySpec);

    CipherOutputStream cos = new CipherOutputStream(new FileOutputStream("abc"), localCipher);
    IOUtils.write("abc", cos);
    cos.flush();
