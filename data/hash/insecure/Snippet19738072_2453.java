MessageDigest md = MessageDigest.getInstance("SHA-1");
byte[] sha1hash = new byte[40];
md.update(text.getBytes("UTF-8"), 0, text.length()); // TODO verify the lengths are the same
sha1hash = md.digest();
