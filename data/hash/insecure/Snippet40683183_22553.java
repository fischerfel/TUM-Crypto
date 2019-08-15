 MessageDigest digester = MessageDigest.getInstance("SHA-1");
 value = digester.digest(password.getBytes());
 digester.update(email.getBytes());
 value = digester.digest(value);
