byte[] key = {0};

byte[] iv = {0, 0, 0, 0, 0, 0, 0, 0};

Cipher c = Cipher.getInstance("BlowFish/CBC/NoPadding");
Key k = new SecretKeySpec(key, "BlowFish");
c.init(Cipher.ENCRYPT_MODE, k, new IvParameterSpec(iv));

byte[] encrypt = c.doFinal("1234567890ABCDEF".getBytes("UTF-8"));

System.out.println(new String(Base64.encodeBase64(encrypt)));
