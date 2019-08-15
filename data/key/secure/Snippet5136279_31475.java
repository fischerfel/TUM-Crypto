SecretKeySpec skeySpec = new SecretKeySpec(key, "AES");
cipher = Cipher.getInstance("AES");
cipher.init(Cipher.ENCRYPT_MODE, skeySpec );
