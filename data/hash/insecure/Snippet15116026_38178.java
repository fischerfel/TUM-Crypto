MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
digest.update(myObject);
byte[] hash = digest.digest();
