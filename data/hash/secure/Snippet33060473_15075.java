MessageDigest messageDigest;
messageDigest = MessageDigest.getInstance("SHA-256");

String input = new String("ALIBABA");
messageDigest.update(input.getBytes(Charset.forName("UTF-8")));
byte[] hash = messageDigest.digest();
String hash1s = new String(hash,StandardCharsets.UTF_8);
System.out.println("HASH 1 is "+hash1s);
System.out.println("HASH 1 is "+hash);

String input2 = new String("ALIBABA");
messageDigest.update(input2.getBytes(Charset.forName("UTF-8")));
byte[] hash2 = messageDigest.digest();
String hash2s = new String(hash2,StandardCharsets.UTF_8);
System.out.println("HASH 2 is "+hash2s);
System.out.println("HASH 2 is "+hash2);
