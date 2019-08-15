String message = "somestring";
byte[] sha1 = MessageDigest.getInstance("SHA1").digest(message.getBytes());
System.out.println(Arrays.toString(message.getBytes()));
System.out.println(Arrays.toString(sha1));
System.out.println(new String(sha1));
