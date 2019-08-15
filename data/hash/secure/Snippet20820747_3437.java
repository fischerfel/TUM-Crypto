String secret = "lT4fhviR7ILvwGeiBJgolfYji1uz/f7B6HQWaWQWVl/sWEz3Kwt4QjzCHWE+MBENOmtgBS6PlN87s+1d7/8bRw==";
String nonce = "1388256620813308";
String postdata = "nonce=1388256620813308";
String path = "/0/private/Balance";

// hash nonce + data
MessageDigest md = MessageDigest.getInstance("SHA-256");
md.update((nonce+postdata).getBytes());
byte[] digest = md.digest();

String baseString = path + new String(digest); //this is probably root of evil

// HMAC
Mac mac = Mac.getInstance("HmacSHA512");
SecretKey secretKey = new SecretKeySpec(Base64.decode(secret, Base64.DEFAULT), "HmacSHA512");
mac.init(secretKey);
String sign = new String(Base64.encodeToString(mac.doFinal(baseString.getBytes()), Base64.DEFAULT)).trim(); 

Log.d(TAG, sign);
