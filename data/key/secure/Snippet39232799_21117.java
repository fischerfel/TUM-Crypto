   public byte[] encrypt(byte[] skey, byte[] data){
      SecretKeySpec skeySpec = new SecretKeySpec(skey, "AES");
      Cipher cipher;
      byte[] encrypted=null;
        try {
            // Get Cipher instance for AES algorithm
            cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");    
            // Initialize cipher
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
            // Encrypt the image byte data
            encrypted = cipher.doFinal(data);
        }catch(Exception e){
            e.printStackTrace();
        }   
      return encrypted;
   }
