MessageDigest md = MessageDigest.getInstance("SHA-256");
String password = "some password";

md.update(password.getBytes("UTF-8"));
byte[] digest = md.digest();
