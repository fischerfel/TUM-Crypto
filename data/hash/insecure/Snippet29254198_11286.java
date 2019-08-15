MessageDigest md = MessageDigest.getInstance("MD5");

String toHash = "HashThis";
md.update(toHash.getBytes());
byte[] isHashed = md.digest();
