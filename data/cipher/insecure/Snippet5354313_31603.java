public static final byte[] IV = { 65, 1, 2, 23, 4, 5, 6, 7, 32, 21, 10, 11, 12, 13, 84, 45 };
public static final byte[] IV2 = { 65, 1, 2, 23, 45, 54, 61, 81, 32, 21, 10, 121, 12, 13, 84, 45 };
public static final byte[] KEY = { 0, 42, 2, 54, 4, 45, 6, 7, 65, 9, 54, 11, 12, 13, 60, 15 };
public static final byte[] KEY2 = { 0, 42, 2, 54, 43, 45, 16, 17, 65, 9, 54, 11, 12, 13, 60, 15 };
//public static final int BITS = 256;

public static void test()
{
    try
    {
        // encryption
        Cipher c = Cipher.getInstance("AES");
        SecretKeySpec keySpec = new SecretKeySpec(KEY, "AES");
        c.init(Cipher.ENCRYPT_MODE, keySpec, new IvParameterSpec(IV));

        String s = "Secret message";
        byte[] data = s.getBytes();

        byte[] encrypted = c.doFinal(data);

        String encryptedStr = "";
        for (int i = 0; i < encrypted.length; i++)
            encryptedStr += (char) encrypted[i];


        //decryoption
        Cipher d_c = Cipher.getInstance("AES");
        SecretKeySpec d_keySpec = new SecretKeySpec(KEY, "AES");
        d_c.init(Cipher.DECRYPT_MODE, d_keySpec, new IvParameterSpec(IV2));

        byte[] decrypted = d_c.doFinal(encrypted);
        String decryptedStr = "";
        for (int i = 0; i < decrypted.length; i++)
            decryptedStr += (char) decrypted[i];
        Log.d("", decryptedStr);

    }
    catch (Exception ex)
    {
        Log.d("", ex.getMessage());
    }
}
