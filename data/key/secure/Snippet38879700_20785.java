KeyGenerator kg = KeyGenerator.getInstance("AES");
kg.init(128, new SecureRandom());
SecretKeySpec aesKey = new SecretKeySpec(kg.generateKey().getEncoded(), "AES");
