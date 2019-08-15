MessageDigest digest = MessageDigest.getInstance("MD5");
digest.update(password.getBytes());
BASE64Encoder encoder = new BASE64Encoder();
byte hashedBytes[] = (new String(digest.digest(), "UTF-8")).getBytes();
System.out.println(encoder.encode(hashedBytes))
