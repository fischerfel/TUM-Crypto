dcipher.init(2, new SecretKeySpec(key, "DESede"), new IvParameterSpec(ecipher.getIV()));
