val md = java.security.MessageDigest.getInstance("SHA-1")
val ha = new sun.misc.BASE64Encoder().encode(md.digest(params.get("Foo").getBytes))
