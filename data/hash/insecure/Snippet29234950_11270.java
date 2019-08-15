 public String MD5(String md5)  {
   try {

       String dat1 = md5.trim();
        java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
        byte[] array = md.digest(dat1.getBytes("UTF-16"));
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < array.length; ++i) {
          sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1,3));
       }
        System.out.println("Digest(in hex format):: " + sb.toString());
        return sb.toString();
    } catch (java.security.NoSuchAlgorithmException e) {
    }
   catch(UnsupportedEncodingException e)
   {
   }
    return null;
}
