    public static String tryDigestAuthentication(String username, String password,String REQUEST_TYPE,String cNounce,String mUri)
    {
        /*String auth = input.getHeaderField("WWW-Authenticate");
        if(auth == null || !auth.startsWith("Digest ")){
            return null;
        }
        final HashMap<String, String> authFields = splitAuthFields(auth.substring(7));*/

        MessageDigest md5 = null;
        try{
            md5 = MessageDigest.getInstance("MD5");
        }
        catch(NoSuchAlgorithmException e){
            return null;
        }

        Joiner colonJoiner = Joiner.on(':');

        String HA1 = null;
        String HATemp = null;
        try{
            md5.reset();
            String ha1str = colonJoiner.join(username,
                "Private", password);
            md5.update(ha1str.getBytes());
            byte[] ha1bytes = md5.digest();
            HATemp = bytesToHexString(ha1bytes);

            Log.e("HATTEMP-->",newByteToHex(ha1bytes));

            String haTstr = colonJoiner.join(HATemp, "WpcHS2/TBAA=dffcc0dbd5f96d49a5477166649b7c0ae3866a93",cNounce);
            md5.update(haTstr.getBytes("ISO-8859-1"));
            byte[] haTbytes = md5.digest();
            HA1 = bytesToHexString(haTbytes);

            Log.e("HA1-->",newByteToHex(haTbytes));
        }
        catch(UnsupportedEncodingException e){
            return null;
        }

        String HA2 = null;
        try{
            md5.reset();
            String ha2str = colonJoiner.join(REQUEST_TYPE,mUri);
            md5.update(ha2str.getBytes("ISO-8859-1"));
            HA2 = bytesToHexString(md5.digest());

            Log.e("HAT2-->",newByteToHex(md5.digest()));

        }
        catch(UnsupportedEncodingException e){
            return null;
        }

        String HA3 = null;
        try{
            md5.reset();
            String ha3str = colonJoiner.join(HA1, "WpcHS2/TBAA=dffcc0dbd5f96d49a5477166649b7c0ae3866a93","00000001" ,
                    cNounce,"auth",HA2);
            md5.update(ha3str.getBytes("ISO-8859-1"));
            HA3 = bytesToHexString(md5.digest());

            Log.e("HAT3-->",newByteToHex(md5.digest()));
        }
        catch(UnsupportedEncodingException e){
            return null;
        }

        StringBuilder sb = new StringBuilder(128);
        sb.append("Digest ");
        sb.append("cnonce"   ).append("=\"").append(cNounce).append("\",");
        sb.append("uri"     ).append("=\"").append(mUri).append("\",");
        //sb.append("qop"     ).append('='  ).append("auth"                  ).append(",");
        sb.append("response").append("=\"").append(HA3                     ).append("\"");

//      try{
//          final HttpURLConnection result = (HttpURLConnection)input.getURL().openConnection();
//          result.addRequestProperty("Authorization", sb.toString());
//          return result;
//      }
//      catch(IOException e){
//          return null;
//      }
        return sb.toString();

    } 
