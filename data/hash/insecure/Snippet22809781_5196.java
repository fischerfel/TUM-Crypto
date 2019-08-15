public static String a(String paramString)
    {

      try
      {
        MessageDigest localMessageDigest = MessageDigest.getInstance("MD5");
        localMessageDigest.update(paramString.getBytes());
        byte[] arrayOfByte2 = localMessageDigest.digest();
       String str = new BigInteger(1, arrayOfByte2).toString(16);
         return str;
      }
      catch (NoSuchAlgorithmException localNoSuchAlgorithmException)
      {
       return "";
      }
    }
