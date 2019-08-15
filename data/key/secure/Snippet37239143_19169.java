SecretKeySpec key = new SecretKeySpec(client_secret.getBytes("UTF-8"), "HmacSHA256");

Mac mac = Mac.getInstance("HmacSHA256"); 

mac.init(key); 

byte[] bytes = mac.doFinal(baseString.getBytes("UTF-8"));

String signature = new String(Base64.encodeBase64(bytes));

log.info("signature="+signature);
