cipher = Cipher.getInstance("AES");
SecretKeySpec spec = new SecretKeySpec("KeyOfSize16Leng.".getBytes("UTF-8"), "AES");
cipher.init(Cipher.DECRYPT_MODE, spec);
