   Properties properties = new Properties();
    CryptoCipher crypt = CryptoCipherFactory.getCryptoCipher("AES/CBC/PKCS5Padding", properties);
    MessageDigest digest = MessageDigest.getInstance("SHA-256");

    byte[] hashedKeyBytes = digest.digest("SHARED_SECRET".getBytes(
            StandardCharsets.UTF_8));
    MessageDigest ivDigest = MessageDigest.getInstance("MD5");

    byte[] ivBytes = ivDigest.digest("SHARED_SECRET".getBytes(StandardCharsets.UTF_8));
    final SecretKeySpec key = new SecretKeySpec(hashedKeyBytes, "AES");
    IvParameterSpec iv = new IvParameterSpec(ivBytes);

    crypt.init(Cipher.DECRYPT_MODE, key, iv);

    ByteBuffer encBuffer = ByteBuffer.allocateDirect(enc.length);
    System.out.println("--" + enc.length);
    encBuffer.put(enc);
    encBuffer.flip();
    System.out.println("encln " + encBuffer.limit());

    ByteBuffer decoded = ByteBuffer.allocateDirect(bufferSize);
    CryptoCipher crypt = init();

    System.out.println("consume " + crypt.update(encBuffer, decoded));
    System.out.println("finish " + crypt.doFinal(encBuffer, decoded));
    decoded.flip(); 
    return asString(decoded);
