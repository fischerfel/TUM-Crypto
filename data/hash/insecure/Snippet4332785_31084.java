byte[] data = hexStringToByteArray(hexData);
MessageDigest md = MessageDigest.getInstance("SHA-1");
md.update(data, 0, data.length);
byte[] sha1hash = md.digest();
