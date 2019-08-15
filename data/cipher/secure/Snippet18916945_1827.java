cipher = Cipher.getInstance("AES/CBC/NoPadding");
cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(rawCipherKey, "AES"), new IvParameterSpec(rawCipherIV));
