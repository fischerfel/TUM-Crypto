MessageDigest digest = MessageDigest.getInstance("SHA1");
String s = "Trolololoooo";
DigestInputStream stream = new DigestInputStream(new ByteArrayInputStream(s.getBytes("UTF-8")), digest)
byte[] digest2 = stream.getMessageDigest().digest();

// timestamp stuff is all org.bouncycastle.tsp.*
TimeStampRequestGenerator timeStampRequestGenerator = new TimeStampRequestGenerator();
timeStampRequestGenerator.setReqPolicy(String.valueOf(new ASN1ObjectIdentifier("1.3.6.1.4.1.13762.3")));
TimeStampRequest timeStampRequest = timeStampRequestGenerator.generate(TSPAlgorithms.SHA1, digest2, BigInteger.valueOf(666));
byte request[] = timeStampRequest.getEncoded();
