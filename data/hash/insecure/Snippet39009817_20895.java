    String ps="tes";
    MessageDigest md = MessageDigest.getInstance("MD5");
    byte[] encryptedPassword = md.digest(ps.getBytes());
    byte[] encodedBytes = Base64.encodeBase64(encryptedPassword);
    String Str2 = new String(encodedBytes);
