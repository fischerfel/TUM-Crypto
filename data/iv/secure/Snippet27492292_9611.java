byte[] sessionKey = SENDER_KEY.getBytes(StandardCharsets.UTF_8);
byte[] iv = SENDER_KEY.getBytes(StandardCharsets.UTF_8) ;
Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");

IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);
SecretKeySpec secretKeySpec = new SecretKeySpec(sessionKey, "AES");

cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec);
byte[] cipheredText = cipher.doFinal(stringedXmlForSending.getBytes());
