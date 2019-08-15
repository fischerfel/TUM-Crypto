    MessageDigest md = MessageDigest.getInstance("SHA1", "BC");
    Cipher cipher = Cipher.getInstance("RSA/None/NoPadding", "BC");
    cipher.init(Cipher.ENCRYPT_MODE, privateKey);
    byte[] hash = md.digest("message".getBytes());
    byte[] signature = cipher.doFinal(hash);
