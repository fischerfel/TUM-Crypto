   String hashValue = MD5Hash("URL or HTML".getBytes());


  /**
     * MD5 implementation as Hash value 
     * 
     * @param a_sDataBytes - a original data as byte[] from String
     * @return String as Hex value 
     * @throws NoSuchAlgorithmException 
     */

    public static String MD5Hash(byte[] dataBytes) throws NoSuchAlgorithmException {
        if( dataBytes == null) return "";

        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(dataBytes);
        byte[] digest = md.digest();

        // convert it to the hexadecimal 
        BigInteger bi = new BigInteger(digest);
        String s = bi.toString(16);
        if( s.length() %2 != 0)
        {
            s = "0"+s;
        }
        return s;
    }
