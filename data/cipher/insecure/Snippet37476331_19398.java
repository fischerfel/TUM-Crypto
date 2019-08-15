String key  = "50B62ECEF1B777353372A44CDDC463987815F783E39D68B8EE6A0AB74A79C7FA";
byte[] keyBytes = key.getBytes("UTF-8");
SecretKey keySpec = new SecretKeySpec(keyBytes, "AES");
Cipher cipher = Cipher.getInstance("AES/ECB/PKCS7Padding");
cipher.init(Cipher.DECRYPT_MODE, keySpec);
buffer = cipher.doFinal(buffer);
