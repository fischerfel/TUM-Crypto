String userid = "AmilaI";
String time = gmtFormat.format(now)+ "Z";

String algorithmKey = time + userid;

SecretKeySpec sks = new SecretKeySpec(algorithmKey.getBytes("UTF-8"), "HmacSHA1");
Mac mac = Mac.getInstance("HmacSHA1");
mac.init(sks);
byte[] hashBytes = mac.doFinal(route.getBytes("UTF-8"));

String hmac = Base64.encodeBase64String(hashBytes);
hmac = hmac.replaceAll("\r\n", "");
System.out.println("Encrypted "+ hmac );

byte[] decoded = Base64.decodeBase64(hmac);
System.out.println("Decrypted " + new String(decoded, "UTF-8") + "\n");
