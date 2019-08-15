    String pass = "1234";
    MessageDigest crypt = null;

    try {
        crypt = java.security.MessageDigest.getInstance("MD5");
    } catch (NoSuchAlgorithmException e) {
        System.out.println("MD5 not supported");
        return; // depends on your method
    }

    byte[] digested = crypt.digest(pass.getBytes());
    String crypt_password = new String();

    // Converts bytes to string
    for (byte b : digested) 
        crypt_password += Integer.toHexString(0xFF & b);

    System.out.println(crypt_password);
