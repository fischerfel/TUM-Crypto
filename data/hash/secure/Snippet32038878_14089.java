MessageDigest md = MessageDigest.getInstance("SHA-256");
String secret = "secret";
md.update(secret.getBytes("UTF-8"));
byte[] key = md.digest();
