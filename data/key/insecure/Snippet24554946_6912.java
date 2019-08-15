Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
SecretKeySpec secretKey = new SecretKeySpec("0123465789".getBytes("UTF-8"), "HmacSHA256");      
sha256_HMAC.init(secretKey);
byte[] hash = sha256_HMAC.doFinal("ABCDEF".getBytes("UTF-8"));      
String check = (new String(Hex.encodeHex(hash))).toUpperCase();
System.out.println(check);

Output
46F9FD56BDAE29A803BAD5BC668CB78DA4C54A51E6C031FB3BC2C42855047213
