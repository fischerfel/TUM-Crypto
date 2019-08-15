String saltedPassword = password + salt;
MessageDigest md = MessageDigest.getInstance("MD5");
md.update(saltedPassword .getBytes());
byte byteData[] = md.digest();
