String oid = "1.2.3.4.5";
String digestAlgorithmName = getDigestAlgorithmName(oid);

MessageDigest messageDigest = MessageDigest.getInstance(digestAlgorithmName);
byte[] actualHash = messageDigest.digest(new byte[] { 0x00 });
