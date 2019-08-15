// an excerpt from the Guava sourcecode
private static MessageDigest getMessageDigest(String algorithmName) {
  try {
    return MessageDigest.getInstance(algorithmName);
  } catch (NoSuchAlgorithmException e) {
    throw new AssertionError(e);
  }
}
