// register the BouncyCastleProvider with the Security Manager
Security.addProvider(new BouncyCastleProvider());

String plainString = "Plaintext Secret";

MessageDigest messageDigest = MessageDigest.getInstance("SHA-512");
byte[] hashedString = messageDigest.digest(plainString.getBytes());

doSomething().with(hashedString);
