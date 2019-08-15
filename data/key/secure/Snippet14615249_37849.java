public static byte[] encryptBytes(byte[] bytes, byte[] key)
{
    Cipher cipher = null;

    try
    {
        cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        SecretKeySpec secretKey = new SecretKeySpec(key, "AES");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);

        return Base64.encodeBase64(cipher.doFinal(bytes));
    }
    catch (Exception e)
    {
        e.printStackTrace();
    }

    return null;
}

public static byte[] decrpytBytes(byte[] encryptedData, String key)
{
    byte[] keyBytes = convertToByteArray(key);
    Cipher cipher = null;

    try
    {
        cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
        SecretKeySpec secretKey = new SecretKeySpec(keyBytes, "AES");
        cipher.init(Cipher.DECRYPT_MODE, secretKey);

        return cipher.doFinal(Base64.decodeBase64(encryptedData));
    }
    catch (Exception e)
    {
        e.printStackTrace();
    }

    return null;
}
//Simply takes every other two characters an terms them into a byte value 
    //then stuffs them into  a byteArray
public static byte[] convertToByteArray(String key)
{
    byte[] b = new byte[key.length()/2];

    for(int i=0, bStepper=0; i<key.length()+2; i+=2)
        if(i !=0)
            b[bStepper++]=((byte) Integer.parseInt((key.charAt(i-2)+""+key.charAt(i-1)), 16));

    return b;
}

public static void main(String[] args) throws Exception
{
            //This string has 64 characters. When sent to convertToByteArray it returns a byte array or 32 bytes
    String key = "00112233445566778899AABBCCDDEEFF0123456789ABCDEF0123456789ABCDEF";

            //Test it out
    byte f[] = {2,4,7};
    byte[] encrypted = encryptBytes(f, convertToByteArray(key));
    byte[] unencrypted = decrpytBytes(encrypted, key);

    System.out.print(unencrypted[0]);
}
