  SecretKey key = new SecretKeySpec(map.get("key"), 0, map.get("key").length, "AES");
  Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
  cipher.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(map.get("IV"), 0, 16));
