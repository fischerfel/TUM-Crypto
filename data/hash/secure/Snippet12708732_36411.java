MessageDigest md = MessageDigest.getInstance("SHA-256");
byte[] digest = md.digest("testString".getBytes());
// ^^ this is where the difference is?
String b64url = Base64.encodeBase64URLSafeString(digest);
// b64url: Ss8LOdnEdmcJo2ifVTrAGrVQVF_6RUTfwLLOqC-6AqM
