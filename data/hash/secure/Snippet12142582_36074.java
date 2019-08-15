    byte[] b = "test".getBytes("ASCII");
    MessageDigest md = MessageDigest.getInstance("SHA-256");
    String bla = new String(md.digest(b), "ASCII");
