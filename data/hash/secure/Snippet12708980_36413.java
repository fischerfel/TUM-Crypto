MessageDigest md = MessageDigest.getInstance("SHA-256");
byte[] digest = md.digest("testString".getBytes()); // Missing charset
String hex = Hex.encodeHexString(digest);
String base64 = Base64.encodeBase64(hex.getBytes());
