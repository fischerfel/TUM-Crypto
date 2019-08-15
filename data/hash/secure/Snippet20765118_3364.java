    String password = "123";
    byte passwordByte[] = password.getBytes();
    MessageDigest md = MessageDigest.getInstance("SHA-512");
    byte passwortHashByte[] = md.digest(passwordByte);
