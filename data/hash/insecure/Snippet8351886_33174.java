// get MD5 base64 hash
MessageDigest messageDigest = MessageDigest.getInstance("MD5");
messageDigest.reset();
messageDigest.update(IOUtils.toByteArray(stream));
byte[] resultByte = messageDigest.digest();
String hashtext = new String(Hex.encodeHex(resultByte));

ObjectMetadata meta = new ObjectMetadata();
meta.setContentLength(IOUtils.toByteArray(stream).length);
meta.setContentMD5(hashtext);
