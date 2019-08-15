DigestInputStream sha = new DigestInputStream(fr, MessageDigest.getInstance("SHA"));
byte[] digest = sha.getMessageDigest();

for(..)
{
result = result + digest[i]
}
