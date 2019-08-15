private String DEFAULT_KEY = "abcdwAYserXbzcSeqL/zPg==";
private String text = "abc";
Base64 base64decoder = new Base64();
byte[] raw = base64decoder.decode(key);

SecretKeySpec fSecretKeySpec = new SecretKeySpec(raw, "AES");

Cipher cipher = Cipher.getInstance("AES");
cipher.init(Cipher.ENCRYPT_MODE, fSecretKeySpec);
byte[] encrypted = cipher.doFinal(text.getBytes());

Base64 base64encoder = new Base64();
result = base64encoder.encodeToString(encrypted);
System.out.println("result: "+ result);
