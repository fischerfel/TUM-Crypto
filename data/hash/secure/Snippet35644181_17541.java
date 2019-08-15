    try {
        return MessageDigest.getInstance(algorithm);
    }
    catch (NoSuchAlgorithmException e) {
        throw new IllegalArgumentException("No such algorithm [" + algorithm + "]");
    }
