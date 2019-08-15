byte[] encodedKey = getKey();
    SecretKeySpec skeySpec = new SecretKeySpec(encodedKey, "AES");
    AlgorithmParameterSpec paramSpec = new IvParameterSpec(initializationVector);

    Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
    cipher.init(Cipher.ENCRYPT_MODE, skeySpec, paramSpec);

    int blockSize = cipher.getBlockSize();
    int diffSize = decrypted.length % blockSize;
    System.out.println("Cipher size: " + blockSize);
    System.out.println("Current size: " + decrypted.length);
    if (diffSize != 0) {
      diffSize = blockSize - diffSize;
      byte[] oldDecrypted = decrypted;
      decrypted = new byte[decrypted.length + diffSize];
      System.arraycopy(oldDecrypted, 0, decrypted, 0, oldDecrypted.length);
      for (int i = 0; i < diffSize; i++) {
        decrypted[i + oldDecrypted.length] = " ".getBytes()[0];
      }
      System.out.println("New size: " + decrypted.length);

    }
    return cipher.doFinal(decrypted);
