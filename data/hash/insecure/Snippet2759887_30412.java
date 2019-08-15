MessageDigest md = MessageDigest.getInstance("MD5");
// hash data...
byte[] hashValue = md.digest();
BigInteger n = new BigInteger(1, hashValue);
n = n.mod(m);
// at that point, n has a value between 0 and m-1 (inclusive)
