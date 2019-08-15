    public String encrypt(SecretKey key, String message){ 
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");              
        cipher.init(Cipher.ENCRYPT_MODE, key);        
        byte[] stringBytes = message.getBytes("UTF8");       
        byte[] raw = cipher.doFinal(stringBytes);

        // converts to base64 for easier display.
        BASE64Encoder encoder = new BASE64Encoder();
        String base64 = encoder.encode(raw);

        return base64;

    }
