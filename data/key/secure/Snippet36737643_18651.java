    SecretKeySpec key = new SecretKeySpec(keyBytes, "AES");
    Cipher cipher = Cipher.getInstance("AES/CTR/NoPadding", "BC");
    cipher.init(Cipher.ENCRYPT_MODE, key);
    byte[] encrypted=cipher.doFinal(msgBytes);
