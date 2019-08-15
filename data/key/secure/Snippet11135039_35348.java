byte[] keyBytes = mysecret.getBytes(); // say mysecret is a String var
final SecretKey key = new SecretKeySpec(keyBytes, HMAC_ALGORITHM);
