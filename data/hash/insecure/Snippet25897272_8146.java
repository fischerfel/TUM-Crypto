MessageDigest digest = MessageDigest.getInstance("MD5");
digest.update(inputPassword.getBytes());
byte[] hashedPassword = digest.digest();
