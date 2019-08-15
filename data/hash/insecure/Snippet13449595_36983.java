  public static String getHashCode(String text) 
 { 
    MessageDigest md;
    byte[] md5hash = new byte[32];
    try{
    md = MessageDigest.getInstance("MD5");

    md.update(text.getBytes("iso-8859-1"), 0, text.length());
    md5hash = md.digest();
    }
    catch(Exception e)
    {
        return "-1";
        }
    String encoded = Base64.encode(md5hash);
    String retValue = new String(encoded);


    return retValue;

   } 
