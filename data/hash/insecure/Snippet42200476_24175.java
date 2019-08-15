private String getString(byte[] bytes)
   {
     StringBuffer sb = new StringBuffer();
   for (int i = 0; i < bytes.length; i++)
     {
       byte b = bytes[i];
       sb.append(0xFF & b);
     }
     return sb.toString();
   }

   public String encrypt(String source)
   {
     try
     {
       MessageDigest md = MessageDigest.getInstance("MD5");
       byte[] bytes = md.digest(source.getBytes());
       return getString(bytes);
     }
     catch (Exception e)
     {
       e.printStackTrace(); }
     return null;
   }
