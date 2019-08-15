public static String encrypter(String value) {   
        try {   
            SecretKeySpec key = new SecretKeySpec("My_Secret_Key".getBytes(), "Blowfish");
            Cipher cipher = Cipher.getInstance("Blowfish");   
            cipher.init(Cipher.ENCRYPT_MODE, key);   
            byte[] plainTxtBytes = value.getBytes("UTF-8");   
            byte[] encBytes = cipher.doFinal(plainTxtBytes);   
            return new sun.misc.BASE64Encoder().encode(encBytes);   
        } catch (Exception ex) {   
            return value;   
        }   
    }   

    public static String decrypter(String value) {   
        try {   
            SecretKeySpec key = new SecretKeySpec("My_Secret_Key".getBytes(), "Blowfish");
            Cipher cipher = Cipher.getInstance("Blowfish");   
            cipher.init(Cipher.DECRYPT_MODE, key);   
            byte[] encBytes = new sun.misc.BASE64Decoder().decodeBuffer(value);   
            byte[] plainTxtBytes = cipher.doFinal(encBytes);   
            return new String(plainTxtBytes);   
        } catch (Exception ex) {   
            return value;   
        }   
  }
