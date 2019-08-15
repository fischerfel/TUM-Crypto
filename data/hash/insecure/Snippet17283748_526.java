MessageDigest messageDigest  = MessageDigest.getInstance("SHA-1");
    byte[] digest = messageDigest.digest((PASSWORD + challenge).getBytes());

    String result = new BigInteger(1, digest).toString(16);
