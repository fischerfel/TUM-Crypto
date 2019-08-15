    MessageDigest dig = MessageDigest.getInstance("MD5");
    DigestInputStream stream = new DigestInputStream(new FileInputStream(new File("/path/to/file")),dig);
    while(stream.read() != -1);
    byte[] digest = dig.digest();
    String md5 = new BigInteger(1, digest).toString(16);
    System.out.print(md5);
