   public static SecretKey generatedessecretkey(String password) throws InvalidKeyException, UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeySpecException
      {
        DESKeySpec keySpec = new DESKeySpec(password.getBytes("UTF8"));
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
        SecretKey key = keyFactory.generateSecret(keySpec);
        return key;
      }

    public static void encrypt(IOLogger log, byte[] datablock, String grouppw, ArrayList<byte[]> resp)
      {
        try
          {
            SecretKey ks = generatedessecretkey(grouppw);
            Cipher cipher = Cipher.getInstance("DES");
            cipher.init(Cipher.ENCRYPT_MODE, ks);
            byte[] b = cipher.doFinal(datablock);
            resp.clear();
            resp.add(b);
            return;
          }
        catch (Exception e)
          {
          }
      }
