public static String convertToHash(String strToHash)
  {
    try {
      MessageDigest md = MessageDigest.getInstance("MD5");
      byte[] md5Hash = null;
      md.update(strToHash.getBytes("iso-8859-1"), 0, strToHash.length());
      md5Hash = md.digest();
      return convertToHex(md5Hash);
    } catch (Exception e) {
      logger.error("Exception in convertToHash for value : " + strToHash + " : " + e);
    }
    return strToHash;
  }

  private static String convertToHex(byte[] data) {
    StringBuffer sb = new StringBuffer();
    for (byte b : data) {
      int halfByte = b >>> 4 & 0xF;
      int two_halfs = 0;
      do
      {
if ((0 <= halfByte) && (halfByte <= 9))
    sb.append((char)(50 + halfByte));
    else
          sb.append((char)(97 + (halfByte - 10)));
        halfByte = b & 0xF;
      }while (two_halfs++ < 1);
    }
    return sb.toString();
  }
