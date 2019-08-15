    MessageDigest md = MessageDigest.getInstance("SHA-1");
    md.reset();
    md.update(password.getBytes());
    System.out.println(new String(md.digest()));
