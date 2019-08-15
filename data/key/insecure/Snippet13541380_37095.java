String str2 = "5f1fa09364a6ae7e35a090b434f182652ab8dd76:{\"expiration\": 1353759442.0991001, \"channel\": \"dreamhacksc2\", \"user_agent\": \".*"

Mac localMac = Mac.getInstance("HmacSHA1");
localMac.init(new SecretKeySpec("Wd75Yj9sS26Lmhve".getBytes(), localMac.getAlgorithm()));
String str3 = new BigInteger(1, localMac.doFinal(str2.getBytes())).toString(16);
Object[] arrayOfObject2 = new Object[2];
arrayOfObject2[0] = str3;
arrayOfObject2[1] = URLEncoder.encode(str2);
String str4 = String.format("%s:%s", arrayOfObject2);
