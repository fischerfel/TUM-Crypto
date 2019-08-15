[...]
MessageDigest md = MessageDigest.getInstance("MD5");
md.update(bFile, 0 , bFile.length);
byte[] mdBytes = md.digest();
[...]
