 String   RawData="data";
 String Key="my-key";
byte[] KeyByteArray=Key.getBytes("UTF-8");

byte[] signature=RawData.getBytes("UTF-8");


Mac sha256_HMAC;
sha256_HMAC = Mac.getInstance("HmacSHA256");

SecretKeySpec secret_key = new SecretKeySpec(KeyByteArray, "HmacSHA256");
    sha256_HMAC.init(secret_key);
String ContentBase64String = Base64.encodeToString(sha256_HMAC.doFinal(signature),Base64.URL_SAFE|Base64.NO_WRAP);
