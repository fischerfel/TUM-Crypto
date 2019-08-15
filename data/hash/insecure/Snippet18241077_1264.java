val md = java.security.MessageDigest.getInstance("SHA-1")
val result = new sun.misc.BASE64Encoder().encode(md.digest("user:pass".getBytes))
