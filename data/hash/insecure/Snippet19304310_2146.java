DigestInputStream shaStream = new DigestInputStream(
    inStream, MessageDigest.getInstance("SHA-1"));
DigestInputStream md5Stream = new DigestInputStream(
    shaStream, MessageDigest.getInstance("MD5"));
// VERY IMPORTANT: read from final stream since it's FilterInputStream
byte[] shaDigest = shaStream.getMessageDigest().digest();
byte[] md5Digest = md5Stream.getMessageDigest().digest();
