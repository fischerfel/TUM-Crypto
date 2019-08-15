MessageDigest md5Digest = MessageDigest.getInstance("MD5");
md5Digest.reset();
md5Digest.update(plainText.getBytes());

byte[] digest = md5Digest.digest();

BASE64Encoder encoder = new BASE64Encoder();
hash = encoder.encode(digest);
hash.replace('/','_');
