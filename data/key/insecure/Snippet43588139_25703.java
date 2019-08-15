Key key = new SecretKeySpec("pass".getBytes("UTF-8"), "HmacSHA1");
Mac mac = Mac.getInstance("HmacSHA256");
mac.init(key);
byte[] signature = mac.doFinal(data.getBytes("UTF-8"));
