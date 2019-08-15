SecretKeyFactory factory = SecretKeyFactory.getInstance("PBEWithSHA256And256BitAES-CBC-BC");
KeySpec spec = new PBEKeySpec(secureKey.toCharArray(), SALT.getBytes(), KEY_ITERATION, KEY_LENGTH);
SecretKey tmp = factory.generateSecret(spec);
SecretKey secret = new SecretKeySpec(tmp.getEncoded(), "AES");
