/**
 * @author Babar
 * @since 29-Jun-15.
 */
public class CryptoUtils
{
    private static final String MD5_ALGO = "MD5";


    public static String encryptToMD5(String text)
    {
        try
        {
            java.security.MessageDigest md = java.security.MessageDigest.getInstance(MD5_ALGO);
            md.update(text.getBytes());

            byte[] bytes = md.digest();

            String hex = bytesToHexString(bytes);

            return hex;
        }
        catch (NoSuchAlgorithmException e)
        {
            e.printStackTrace();

            return String.valueOf(text.hashCode());
        }
    }

    private static String bytesToHexString(byte[] bytes)
    {
        StringBuffer sb = new StringBuffer();

        for(int i = 0; i < bytes.length; i++)
        {
            String hex = Integer.toHexString(0xFF & bytes[i]);

            if(hex.length() == 1)
            {
                sb.append('0');
            }

            sb.append(hex);
        }

        return sb.toString();
    }

    public static String encodeToBase64(String str) {
        String tmp = "";
        if(isNotNullOrEmpty(str)) {
            try {
                tmp = new String(Base64.encode(str.getBytes(), Base64.DEFAULT)).trim();
            } catch(Throwable e) {
                e.printStackTrace();
            }
        }
        return tmp;
    }
}
