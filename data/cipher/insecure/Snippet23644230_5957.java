public class DESUtil
{
    private static final String Algorithm = "DESede/ECB/PKCS5Padding";// DESede/ECB/PKCS5Padding;DESede

    private static final String DESede = "DESede";

    public static byte[] encrypt(byte[] keybyte, byte[] src)
    throws NoSuchAlgorithmException, NoSuchPaddingException, Exception
    {
        SecretKey deskey = new SecretKeySpec(keybyte, DESede);
        Cipher c1 = Cipher.getInstance(Algorithm);
        c1.init(Cipher.ENCRYPT_MODE, deskey);
        return c1.doFinal(src);
    }

    public static byte[] decrypt(byte[] keybyte, byte[] src)
        throws NoSuchAlgorithmException, NoSuchPaddingException, Exception
    {
        SecretKey deskey = new SecretKeySpec(keybyte, DESede);
        Cipher c1 = Cipher.getInstance(Algorithm);
        c1.init(Cipher.DECRYPT_MODE, deskey);
        return c1.doFinal(src);
    }

    public static String byte2hex(byte[] b)
    {
        StringBuffer hs = new StringBuffer();
        String stmp = "";
        for (int n = 0; n <b.length; n++)
        {
            stmp = (java.lang.Integer.toHexString(b[n] & 0XFF));
            if (stmp.length() == 1)
                hs.append("0").append(stmp);
            else
                hs.append(stmp);
        }
        return hs.toString().toUpperCase(Locale.getDefault());
    }

    public static byte[] hex2byte(String hexStr)
    {
        if (hexStr.length() % 2 != 0)
        {
            AppLogger.error("hex2bytes's hexStr length is not even.");
            return null;
        }

        byte[] toBytes = new byte[hexStr.length() / 2];
        for (int i = 0, j = 0; i <hexStr.length(); j++, i = i + 2)
        {
            int tmpa = Integer.decode(
                "0X" + hexStr.charAt(i) + hexStr.charAt(i + 1)).intValue();
            toBytes[j] = (byte) (tmpa & 0XFF);
        }
        return toBytes;
    }


    public static void main(String[] args)
    {
        Security.addProvider(new com.sun.crypto.provider.SunJCE());
        final byte[] rawKey = "db90e7eb".getBytes();
        final byte[] keyBytes = new byte[24];

        for (int i = 0; i <rawKey.length; i++)
        {
            keyBytes[i] = rawKey[i];
        }

        for (int i = rawKey.length; i <keyBytes.length; i++)
        {
            keyBytes[i] = (byte)0;
        }

        String szSrc = "20926330$AD75B1697FB5EB6345B2D412124030D2$10086$10086$10.164.111$ABCDEFGH$Reserved$CTC";
        System.out.println("string before encrypt:" + szSrc);
        byte[] encoded = null;

        try
        {
            encoded = encrypt(keyBytes, szSrc.getBytes());
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        System.out.println("string after encrypt::" + byte2hex(encoded));

        byte[] srcBytes = null;

        try
        {
            srcBytes = decrypt(keyBytes, encoded);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        System.out.println("string before decode: :" + (new String(srcBytes)));
    }
}
