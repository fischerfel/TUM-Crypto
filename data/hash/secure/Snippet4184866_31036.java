final MessageDigest mDigest = MessageDigest.getInstance("SHA-224");
byte[] messageDigest = mDigest.digest(toEncrypt.getBytes());
final BigInteger number = new BigInteger(1, messageDigest);
final String sha = number.toString(16);
final int diff = 32 - sha.length();
final StringBuilder finalSHA = new StringBuilder(32);
for (int i=0;i<diff;i++) {
 finalSHA.append("0");
}
finalSHA.append(sha);
return finalSHA.toString();
