byte keySelectedByUser[] = selectedKey.getBytes();
SecretKeySpec secretKey = new SecretKeySpec(keySelectedByUser, "AES");

Cipher cipher;

cipher = Cipher.getInstance("AES/CBC/PKCS7PADDING");

cipher.init(Cipher.ENCRYPT_MODE, secretKey);

byte[] encrypted = cipher.doFinal(stringToEncrypt.getBytes());
