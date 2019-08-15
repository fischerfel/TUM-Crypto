MessageDigest m;
m = MessageDigest.getInstance("MD5");
byte[] UTF8bytes = key.getBytes("UTF8");
m.update(UTF8bytes,0,UTF8bytes.length);
