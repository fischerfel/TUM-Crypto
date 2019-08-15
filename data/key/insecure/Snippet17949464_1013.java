Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
SecretKeySpec secretKey = new SecretKeySpec("1234".getBytes(), "HmacSHA256");
sha256_HMAC.init(secretKey);
byte[] hash = sha256_HMAC.doFinal("test".getBytes());
String check = Hex.encodeHexString(hash);
System.out.println(new String(check));

Output
24c4f0295e1bea74f9a5cb5bc40525c8889d11c78c4255808be00defe666671f
