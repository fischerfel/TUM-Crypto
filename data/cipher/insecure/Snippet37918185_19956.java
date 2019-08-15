public static String encode(String srcStr) {
    if (srcStr == null)
        return null;
    String dst = null;
    byte[] result = encrypt2(srcStr.getBytes(), "h43au76U");
    if (result == null)
        return null;
    System.out.println(result);
    dst = byte2HexStr(result, result.length);
    return dst;
    }

 private static final char[] mChars = "0123456789ABCDEF".toCharArray();


 public static String byte2HexStr(byte[] b, int iLen) {
        if (b == null)
            return null;
        StringBuilder sb = new StringBuilder();
        for (int n = 0; n < iLen; n++) {
            sb.append(mChars[(b[n] & 0xff) >> 4]);
            sb.append(mChars[b[n] & 0xf]);
        }
        return sb.toString().trim().toUpperCase(Locale.US);
        }



 private static byte[] encrypt2(byte[] datasource, String password) {
        byte[] is;
        try {
            SecureRandom random = new SecureRandom();
            DESKeySpec desKey = new DESKeySpec(password.getBytes("UTF-8"));
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            javax.crypto.SecretKey securekey
            = keyFactory.generateSecret(desKey);
            Cipher cipher = Cipher.getInstance("DES");
            cipher.init(1, securekey, random);
            is = cipher.doFinal(datasource);
        } catch (Throwable e) {
            e.printStackTrace();
            return null;
        }
        return is;
        }
