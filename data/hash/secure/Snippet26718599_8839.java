    // Compute digest
    MessageDigest sha1 = MessageDigest.getInstance("SHA-512");
    byte[] digest = sha1.digest(data);

    // Encrypt digest
    Cipher cipher = Cipher.getInstance("RSA");
    cipher.init(Cipher.ENCRYPT_MODE, stringtoPublicKey(key_in_string));
    byte[] cipherText = cipher.doFinal(digest);
