    byte[] buffer= new byte[8192];
    int count;
    MessageDigest digest = MessageDigest.getInstance("SHA-256");
    BufferedInputStream bis = new BufferedInputStream(new FileInputStream(fileName));
    while ((count = bis.read(buffer)) > 0) {
        digest.update(buffer, 0, count);
    }
    byte[] hash = digest.digest();
    System.out.println(new BASE64Encoder().encode(hash));
