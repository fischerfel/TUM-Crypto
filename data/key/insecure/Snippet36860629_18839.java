Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
SecretKeySpec myKey = new SecretKeySpec("mysecretmysecret".getBytes(), "AES");
IvParameterSpec IVKey = new IvParameterSpec("mysecretmysecret".getBytes());
cipher.init(Cipher.ENCRYPT_MODE, myKey, IVKey);
byte[] outputBytes = cipher.doFinal(read(sampleFile));
return outputBytes;
