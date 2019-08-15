 SecretKey keyFromPassword =
        SecretKeyFactory.getInstance(
            algorithm).generateSecret(
            new PBEKeySpec(password.toCharArray()));

 Cipher cipher = Cipher.getInstance(algorithm);
 cipher.init(Cipher.ENCRYPT_MODE, keyFromPassword, new PBEParameterSpec(
        salt, iterations, new IvParameterSpec(iv)));
 IOUtils.copyLarge(new CipherInputStream(clearStream, cipher), encryptedStream);
