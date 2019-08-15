public static byte[] decryptByte(byte[] blahh, byte[] keyExample) throws Exception
{
Cipher cipher = null;

try
{
    cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
    SecretKeySpec secretKey = new SecretKeySpec(key, "AES");
    cipher.init(Cipher.DECRYPT_MODE, secretKey);

    return cipher.doFinal(Base64.decodeBase64(blah));
}
catch(Exception e)
{
    e.printStackTrace();
}
return null;
}

String keyExample = "99112277445566778899AABBCCDDEEFF0123456789ABCDEF0123456789ABCDEF";
byte[] key = keyExample.getBytes();    
byte[] barrayMessage = {123,45,55,23,64,21,65};    
byte[] result = decryptByte(barrayMessage, key);
