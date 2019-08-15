    final MessageDigest md = MessageDigest.getInstance("SHA-256");
    String value = "admin";
    md.update(value.getBytes("UTF-8"));
    final byte bin[] = md.digest();

    System.out.println(DigestUtils.sha256Hex(bin));
    System.out.println(DigestUtils.sha256Hex(value));
