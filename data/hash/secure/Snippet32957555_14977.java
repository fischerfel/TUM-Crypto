final String plaintext = "derp";
final MessageDigest md;
try {
    md = MessageDigest.getInstance("SHA-256");
} catch (NoSuchAlgorithmException e) {
/* SHA-256 should be supported on all devices. */
    throw new RuntimeException(e);
}
final String inputHash = bytesToHex(md.digest(plaintext.getBytes()));
Log.debug(TAG, "input hash: " + inputHash);
