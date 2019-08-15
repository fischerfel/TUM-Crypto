val md = java.security.MessageDigest.getInstance("SHA-1");
println(new sun.misc.BASE64Encoder().encode(md.digest("Foo".getBytes)))
