 public static void desdecrypt(IOLogger log, byte[] datablock, String grouppw, ArrayList<byte[]> resp)
      {
        try
          {
            SecretKey ks = generatedessecretkey(grouppw);
            Cipher cipher = Cipher.getInstance("DES");
            cipher.init(Cipher.DECRYPT_MODE, ks);
            byte[] b = cipher.doFinal(datablock);
            resp.clear();
            resp.add(b);
            return;
          }
        catch (Exception e)
          {
          }
      }
