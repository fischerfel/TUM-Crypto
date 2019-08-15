    final String toHash = id + ts + secret;
    final MessageDigest digest = MessageDigest.getInstance("SHA-256");
    final byte[] hash = digest.digest(toHash.getBytes("UTF-8"));
    final String result = getHexFormated(hash)
