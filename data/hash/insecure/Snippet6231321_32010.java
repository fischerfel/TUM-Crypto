MessageDigest md = MessageDigest.getInstance("MD5");
byte[] md4 = Files.getDigest(localFile, md);
