private String calculateSignature(String sharedSecret, String requestJson) {
  try {
    String contentToDigest = sharedSecret + requestJson;
    MessageDigest messageDigest = MessageDigest.getInstance("SHA-512");
    byte[] digest = messageDigest.digest(contentToDigest.getBytes());
    return Base64.getEncoder().encodeToString(digest);
  } catch (NoSuchAlgorithmException e) {
    throw new RuntimeException("Error calculating digest for payload [" +  requestJson + "]", e);
  }
}
