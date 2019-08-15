MessageDigest md = MessageDigest.getInstance("SHA-1");
byte[] sha1hash = new byte[40];
md.update(text.getBytes("iso-8859-1"), 0, text.length());
sha1hash = md.digest();
