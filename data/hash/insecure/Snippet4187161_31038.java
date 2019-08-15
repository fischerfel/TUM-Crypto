MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
digest.update(...your data here...);
byte[] hash = digest.digest();
