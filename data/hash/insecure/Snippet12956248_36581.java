MessageDigest sha = MessageDigest.getInstance("MD5");                    
byte[] digestedPassword = sha.digest(password.getBytes(Charset.defaultCharset().name()));
return new String(digestedPassword, Charset.defaultCharset().name());
