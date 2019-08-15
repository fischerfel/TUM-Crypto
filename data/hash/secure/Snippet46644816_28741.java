MessageDigest digest = MessageDigest.getInstance("SHA-256");
digest.update(secretKey.getBytes("UTF-8"));

byte[] secretKeyBytes = secretKey.getBytes("UTF-8");

byte[] keyBytes = new byte[16];
System.arraycopy(digest.digest(), 0, keyBytes, 0, keyBytes.length);
