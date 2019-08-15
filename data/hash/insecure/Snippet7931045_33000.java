// convert your hex string to bytes
BigInteger bigInt = new BigInteger(salt, 16);
byte[] bytes = bigInt.toByteArray();
// get the MD5 digest library
MessageDigest md5Digest = null;
try {
    md5Digest = MessageDigest.getInstance("MD5");
} catch (NoSuchAlgorithmException e) {
    // error handling here...
}
// by default big integer outputs a 0 sign byte if the first bit is set
if (bigInt.testBit(0)) {
    md5Digest.update(bytes, 1, bytes.length - 1);
} else {
    md5Digest.update(bytes);
}
// get the digest bytes
byte[] digestBytes = md5Digest.digest();
