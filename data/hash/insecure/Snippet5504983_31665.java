    MessageDigest messageDigest = MessageDigest.getInstance("SHA-1");
    messageDigest.update("messageImprint".getBytes());
    byte[] digest = messageDigest.digest();
