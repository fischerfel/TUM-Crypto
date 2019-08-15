MessageDigest md = MessageDigest.getInstance("SHA1");
md.update(bytesToSign);
byte[] digest = md.digest();
