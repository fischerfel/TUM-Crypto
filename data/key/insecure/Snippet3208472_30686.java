SecretKeySpec keySpec = new SecretKeySpec(
        "qnscAdgRlkIhAUPY44oiexBKtQbGY0orf7OV1I50".getBytes(),
        "HmacSHA1");

Mac mac = Mac.getInstance("HmacSHA1");
mac.init(keySpec);
byte[] result = mac.doFinal("foo".getBytes());

BASE64Encoder encoder = new BASE64Encoder();
System.out.println(encoder.encode(result));
