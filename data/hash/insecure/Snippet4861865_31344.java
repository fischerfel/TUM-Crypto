MessageDigest md = MessageDigest.getInstance("SHA");
md.reset();
byte[] encryptedBinarySource = md.digest(source.getBytes("UTF-8"));
