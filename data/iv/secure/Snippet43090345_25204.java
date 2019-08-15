public static String doDecrypt(String data, String key){
String decryptedData = null;
  try {
      Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
      byte[] keyBytes = new byte[16];
      byte[] b = key.getBytes("UTF-8");
      int len = b.length;
      if (len > keyBytes.length)
           len = keyBytes.length;
           System.arraycopy(b, 0, keyBytes, 0, len);
           SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "AES");
           IvParameterSpec ivSpec = new IvParameterSpec(keyBytes);
           cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);
           byte[] results = new byte[data.length()];

     try {
          results = cipher.doFinal(Base64.decode(data,Base64.DEFAULT));
    } catch (Exception e) {
       Log.i("Erron in Decryption", e.toString());
    }
       Log.i("Data", new String(results, "UTF-8"));
       decryptedData = new String(results, "UTF-8");
     }
       catch (Exception e) {
       e.printStackTrace();
       return null;
    }
      return decryptedData;
    }
