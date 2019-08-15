MessageDigest md = MessageDigest.getInstance("SHA-256");
md.update("some string".getBytes("UTF-8"));
byte[] digest = md.digest();
