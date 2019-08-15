Mac mac = Mac.getInstance("HmacSHA1");
String secretKey ="sKey";
String content ="Hello";

byte[] secretKeyBArr = secretKey.getBytes();    
byte[] contentBArr = content.getBytes();

SecretKeySpec secret_key = new SecretKeySpec(secretKeyBArr,"HmacSHA1");
byte[] secretKeySpecArr = secret_key.getEncoded();

mac.init(secret_key);

byte[] final = mac.doFinal(contentBArr);
