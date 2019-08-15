public class CriptographyUtils
{
    private static final String INIT_VECTOR = "fedcba9876543210";
    private static final String ALGORITHM = "AES/CBC/NoPadding";

    public static String aesEncrypt(String key, String text)  // encrypts text (get bytes -> encrypt -> encode -> to String)
    {
        String result;

        try
        {
            IvParameterSpec iv = new IvParameterSpec(INIT_VECTOR.getBytes());
            SecretKeySpec myKey = new SecretKeySpec(fixKey(key).getBytes("UTF-8"), "AES");

            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, myKey, iv);

            byte[] encryptedBytes = cipher.doFinal(text.getBytes("UTF-8"));

            result = Base64.encodeToString(encryptedBytes, Base64.DEFAULT);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            result = "error";
        }

        return result;
    }

    public static String aesDecrypt(String key, String text)  // decrypts text (get bytes -> decode -> decrypt -> to String)
    {
        String result;

        try
        {
            IvParameterSpec iv = new IvParameterSpec(INIT_VECTOR.getBytes("UTF-8"));
            SecretKeySpec myKey = new SecretKeySpec(fixKey(key).getBytes("UTF-8"), "AES"); // create new KEY in utf-8

            Cipher cipher = Cipher.getInstance(ALGORITHM); // create new cipher
            cipher.init(Cipher.DECRYPT_MODE, myKey, iv); // set cipher into decrypt mode using my KEY

            byte[] decryptedBytes = cipher.doFinal(Base64.decode(text, Base64.DEFAULT)); // get bytes -> decode -> decrypt

            result = new String(decryptedBytes);    // convert decrypted text to String
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            result = "error";
        }

        return result;
    }

    private static String fixKey(String key)
    {
        if (key.length() < 16)  // less than 128 bits
        {
            int numPad = 16 - key.length();

            for (int i = 0; i < numPad; i++)
                key += "0"; //0 pad to len 16 bytes
        }
        else if (key.length() > 16)
            key = key.substring(0, 16); //truncate to 16 bytes

        return key;
    }
}
