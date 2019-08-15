String encPass = "{SHA-1}" + new sun.misc.BASE64Encoder()
.encode(java.security.MessageDigest.getInstance("SHA1")
.digest(newUser.getPassword().getBytes()));
