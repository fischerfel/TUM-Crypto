   MessageDigest md = MessageDigest.getInstance("MD5");
   String md5password = new String(md.digest("test".getBytes("UTF-8")), "UTF-8");
