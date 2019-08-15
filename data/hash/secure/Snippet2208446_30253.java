Security.addProvider(new BouncyCastleProvider());

String data = "hello world";

MessageDigest mda = MessageDigest.getInstance("SHA-512", "BC");
byte [] digesta = mda.digest(data.getBytes());

MessageDigest mdb = MessageDigest.getInstance("SHA-512", "BC");
byte [] digestb = mdb.digest(data.getBytes());

System.out.println(MessageDigest.isEqual(digesta, digestb));

System.out.println(Hex.encodeHex(digesta));
