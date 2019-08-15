Key secretKey = new SecretKeySpec("mysecretmysecret".getBytes(), "AES");
Cipher cipher = Cipher.getInstance("AES");
cipher.init(Cipher.ENCRYPT_MODE, secretKey);
byte[] outputBytes = cipher.doFinal(read(sampleFile));
return outputBytes;
