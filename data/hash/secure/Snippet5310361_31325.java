MessageDigest digest = MessageDigest.getInstance("SHA-256");
digest.update(password.getBytes());
digest.update(salt);
byte[] raw = digest.digest();
