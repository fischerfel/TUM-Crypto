Mac sha256HMAC = javax.crypto.Mac.getInstance("HmacSHA256");
SecretKeySpec secretKey = new SecretKeySpec(teamAPIKEY.getBytes(), "HmacSHA256");
sha256HMAC.init(secretKey);
String encodedHMAC256 = Base64.encodeBase64String(sha256HMAC.doFinal(jsonBody.getBytes("UTF-8")));
