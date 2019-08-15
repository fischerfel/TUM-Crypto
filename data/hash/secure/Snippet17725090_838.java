   MessageDigest md = MessageDigest.getInstance("SHA-512");
   md.reset();
   md.update(saltKey.getBytes());
   md.digest(password.getBytes("UTF-8"));
