    MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
    byte bin[] = messageDigest.digest(password.getBytes("UTF-8"));
    return Base64.encodeBase64String(bin);
