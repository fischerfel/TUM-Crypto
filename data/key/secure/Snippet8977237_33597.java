Mac hmacSha256 = Mac.getInstance("hmacSHA256");
byte[] keyBytes = key.getBytes("UTF-8");                 
Key k = new SecretKeySpec(keyBytes, "hmacSHA256");

hmacSha256.init(k);

byte[] dataBytes = data.getBytes("UTF-8");
byte[] sig = hmacSha256.doFinal(dataBytes)

String sigString = base64Url( sig );
