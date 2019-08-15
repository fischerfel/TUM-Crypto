Cipher cipher = Cipher.getInstance("AES");
cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(Hex.decodeHex(encryptionKey.toCharArray()), "AES"));
encrypted = Hex.encodeHexString(cipher.doFinal((sampleText.toString()).getBytes()));
