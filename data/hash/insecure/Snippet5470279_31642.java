    String input = "168";
    MessageDigest md = MessageDigest.getInstance("MD5");
    byte[] md5sum = md.digest(input.getBytes());
    String output = String.format("%032X", new BigInteger(1, md5sum));
