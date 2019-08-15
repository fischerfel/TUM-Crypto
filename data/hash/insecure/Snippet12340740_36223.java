    MessageDigest md = MessageDigest.getInstance("MD5");
    md.update(str.getBytes("iso-8859-1"), 0, str.length());
