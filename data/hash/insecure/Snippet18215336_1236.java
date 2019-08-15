MessageDigest md = MessageDigest.getInstance("SHA1");
byte[] hash = md.digest(password.getBytes("UTF-8"));
