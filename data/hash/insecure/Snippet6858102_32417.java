byte[] warBytes;
MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
digest.update(warBytes);
byte[] hash = digest.digest();
