
crypt = MessageDigest.getInstance("SHA-1");
crypt.reset();
crypt.update(testData);
byte [] result = crypt.digest();
