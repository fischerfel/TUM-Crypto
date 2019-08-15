    MessageDigest digest = MessageDigest.getInstance("SHA-256");
    byte[] hash = digest.digest("12345".getBytes(StandardCharsets.UTF_8));

    String hex = DatatypeConverter.printHexBinary(hash);
    System.out.println(hex); 
