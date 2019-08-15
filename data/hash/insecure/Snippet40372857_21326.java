String text = "PARAMETER123";

MessageDigest md = MessageDigest.getInstance("SHA-1");
byte[] textBytes = text.getBytes("UTF-8");
md.update(textBytes, 0, textBytes.length);
byte[] sha1hash = md.digest();

String encrypted_text = = convertToHex(sha1hash);
