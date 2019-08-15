MessageDigest md = MessageDigest.getInstance("MD5");
md.update(password.getBytes());
byte byteData[] = md.digest();
