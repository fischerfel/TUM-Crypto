    String secretKey = "abcdefghijklmnopqrstuvwx";

    byte[] bytes = secretKey.getBytes("UTF-8");
    SecretKey key = new SecretKeySpec(bytes, "DESede");

    Cipher ecipher = Cipher.getInstance("DESede");
    ecipher.init(Cipher.ENCRYPT_MODE, key);

    String input = "holahola1"; 

    byte[] utf8 = input.getBytes("UTF8");
    byte[] enc = ecipher.doFinal(utf8); // Encrypt
