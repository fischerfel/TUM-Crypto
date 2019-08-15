SecretKeySpec secretKeySpec = new SecretKeySpec(key, "AES");
Cipher cipher = Cipher.getInstance("AES");
cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
byte[] encData = cipher.doFinal(data, 0, data.length);
Arrays.fill(key, (byte)0);
