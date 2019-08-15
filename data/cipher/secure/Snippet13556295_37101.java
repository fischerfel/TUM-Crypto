String test ="test";
byte[] testbytes = test.getBytes();
Cipher cipher = Cipher.getInstance("RSA");
cipher.init(Cipher.ENCRYPT_MODE, publicKey);
byte[] cipherData = cipher.doFinal(testbytes);
String s = new String(cipherData);
Log.d("testbytes after encryption",s);
