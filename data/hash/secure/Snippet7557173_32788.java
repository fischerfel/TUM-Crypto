java.security.MessageDigest saltDigest = MessageDigest.getInstance("SHA-256");
saltDigest.update(UUID.randomUUID().toString().getBytes("UTF-8"));
String digest = String.valueOf(Hex.encode(saltDigest.digest()));
