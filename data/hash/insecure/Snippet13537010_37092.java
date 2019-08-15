    MessageDigest messageDigest = java.security.MessageDigest.getInstance("MD5");
    digest = messageDigest.digest(bytes);
    String digestString = DigestUtils.md5Hex(digest);
