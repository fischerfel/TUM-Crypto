    MessageDigest md = null;
    try {
        md = MessageDigest.getInstance("SHA-1");
    }
    catch(NoSuchAlgorithmException e) {
        e.printStackTrace();
    } 

    String hashToConvert = byteArrayToHexBigInt(md.digest(objectName));

    /*
     * Converts to big Integer, will not be concatted because structure
     * is large enough to hold the Hash created
     * Second argument is set to 16 as this is the base for the parsing of the
     * hash to convert
     */
    BigInteger bigHash = new BigInteger(hashToConvert, 16);

    return bigHash;

}


public static String byteArrayToHexBigInt(byte[] b) throws Exception {
      String result = "";
      for (int i=0; i < b.length; i++) {
        result +=
              Integer.toString( ( b[i] & 0xff ) + 0x100, 16).substring( 1 );
      }
      return result;
    }
