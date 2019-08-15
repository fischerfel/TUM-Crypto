private static byte[] key = {
    0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09, 0x10, 0x11, 0x12, 0x13, 0x14, 0x15};

 public static String decrypt()
   {

       byte[] info= hexStrToByteArray("1425EC9B5D983FF7DF45A4A8089E69FC"); 
       try
       {
           Cipher cipher = Cipher.getInstance("AES/ECB/NOPADDING");
           final SecretKeySpec secretKey = new SecretKeySpec(key, "AES");
           cipher.init(Cipher.DECRYPT_MODE, secretKey);
           byte[] decryptedResult= cipher.doFinal(info);
           String result = new String(result, "UTF-8");
           return result;
       }
       catch (Exception e)
       {
         e.printStackTrace();

       }
       return null;
   }


  private static byte[] hexStrToByteArray(String hex) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream(hex.length() / 2);

        for (int i = 0; i < hex.length(); i += 2) {
            String output = hex.substring(i, i + 2);
            int decimal = Integer.parseInt(output, 16);
            baos.write(decimal);
        }
        return baos.toByteArray();
    }
