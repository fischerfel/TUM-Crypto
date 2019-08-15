String data = "{\"method\":\"getArtistSearchResults\",\"parameters\":{\"query\":\"adele\"},\"header\":{\"wsKey\":\"concertboom\"}}";

String key = "XXXXX";

SecretKeySpec keySpec = new SecretKeySpec(key.getBytes(), "HmacSHA1");
Mac mac = Mac.getInstance("HmacMD5");
mac.init(keySpec);
byte[] result = mac.doFinal(data.getBytes());

BASE64Encoder encoder = new BASE64Encoder();

signature = encoder.encode(result);
