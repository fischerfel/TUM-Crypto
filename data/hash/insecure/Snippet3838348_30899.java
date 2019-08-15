final MessageDigest messageDigest = MessageDigest.getInstance("MD5");
messageDigest.reset();
messageDigest.update(string.getBytes(Charset.forName("UTF8")));
final byte[] resultByte = messageDigest.digest();
final String result = new String(Hex.encodeHex(resultByte));
