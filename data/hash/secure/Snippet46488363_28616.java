String password = "test";
MessageDigest md = MessageDigest.getInstance("SHA-256");
byte[] sha256Result = md.digest(password.getBytes(StandardCharsets.UTF_8));
String result = new String(sha256Result, StandardCharsets.UTF_8);
