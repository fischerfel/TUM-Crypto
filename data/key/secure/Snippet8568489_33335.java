    String hex = new String(readFileToByteArray(file));
    byte[] encoded = new BigInteger(hex, 16).toByteArray();
    SecretKey key = new SecretKeySpec(encoded, "AES");
    System.out.println("read:" + key.getEncoded().length);
    return key;
