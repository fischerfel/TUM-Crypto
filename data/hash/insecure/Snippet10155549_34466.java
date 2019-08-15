MessageDigest crypt = MessageDigest.getInstance("SHA-1");
crypt.reset();
byte[] buf = crypt.digest("hello, SO how are you?".getBytes());
