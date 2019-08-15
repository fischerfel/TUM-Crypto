String hash = null;
    try
    {
        MessageDigest digest = MessageDigest.getInstance("SHA-1");
        digest.update(toHash.getBytes(), 0, toHash.length());
        hash = new BigInteger(1, digest.digest()).toString(16);
    }
    catch (NoSuchAlgorithmException e)
    {
        e.printStackTrace();
    }
