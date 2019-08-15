import java.security.MessageDigest;

byte[] input = /* ... */;
// getInstance can throw NoSuchAlgorithmException if the algorithm is unsupported
MessageDigest md = MessageDigest.getInstance("MD5" /* or any other algorithm */);
md.digest(input);  // => array of bytes
