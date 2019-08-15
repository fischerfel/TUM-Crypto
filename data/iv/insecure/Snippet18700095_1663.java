   public String decrypt(){
       String keyStr = "password";
       String msg = "KGFL1GG5VLQ=";
       String erg = "";
       try{


       KeySpec ks = new DESKeySpec(keyStr.getBytes("UTF-8"));
       SecretKey key = SecretKeyFactory.getInstance("DES").generateSecret(ks);
       IvParameterSpec iv = new IvParameterSpec(Hex.decodeHex("1234567890ABCDEF".toCharArray()));
       Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
       cipher.init(Cipher.DECRYPT_MODE, key, iv);

       byte[] decoded = cipher.doFinal(Base64.decodeBase64(msg));
       erg = new String(decoded);
       } catch (Exception e){
           erg = "error";
       }
       return erg;

   }
