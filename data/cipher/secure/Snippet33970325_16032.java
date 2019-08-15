    byte[] encrypted = getBytesFromInputStream(in);

    String password = "somepassword";

    Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding", "BC");

    // Openssl puts SALTED__ then the 8 byte salt at the start of the
    // file. We simply copy it out.
    byte[] salt = new byte[8];
    System.arraycopy(encrypted, 8, salt, 0, 8);
    SecretKeyFactory fact = SecretKeyFactory.getInstance("PBEWITHMD5AND256BITAES-CBC-OPENSSL", "BC");
    c.init(Cipher.DECRYPT_MODE, fact.generateSecret(new PBEKeySpec(password.toCharArray(), salt, 100)));

    // Decrypt the rest of the byte array (after stripping off the salt)
    byte[] data = c.doFinal(encrypted, 16, encrypted.length - 16);
