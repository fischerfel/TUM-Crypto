MessageDigest md = MessageDigest.getInstance("MD5");
byte[] md5 = Files.getDigest(localFile, md);
String hashtext = DigestUtils.md5Hex(md5);
