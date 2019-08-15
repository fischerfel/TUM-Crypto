byte[] decodedString = Base64.decode(testimg,  Base64.DEFAULT);

MessageDigest md = MessageDigest.getInstance("MD5");
md.reset();
md.update(decodedString);

byte[] array = md.digest();
