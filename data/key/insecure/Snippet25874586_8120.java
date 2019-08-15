 String keyString = "theKeyImUsing";
 SecretKeySpec macKey = new SecretKeySpec((keyString).getBytes("UTF-8"), "HmacMD5");
 mac.init(macKey);
