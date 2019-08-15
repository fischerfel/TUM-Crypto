MessageDigest sha = MessageDigest.getInstance("SHA-256");
sha.update(in.getBytes());
byte[] digest = sha.digest();
