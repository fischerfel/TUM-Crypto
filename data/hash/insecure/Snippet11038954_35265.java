String encryptionKey = "test";

    MessageDigest messageDigest = MessageDigest.getInstance("MD5");
    messageDigest.update(encryptionKey.getBytes("UTF-8"), 0, encryptionKey.length());
    byte[] encryptionKeyBytes = messageDigest.digest();
