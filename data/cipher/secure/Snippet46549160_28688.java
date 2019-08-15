//encrypt
try {
        Cipher c = Cipher.getInstance("ECIES",BouncyCastleProvider.PROVIDER_NAME);
        c.init(Cipher.ENCRYPT_MODE,publicKey);
        encodeBytes = c.doFinal(origin.getBytes());           
        //encrypt = Base64.getEncoder().encodeToString(encodeBytes);
        encrypt = bytesToHex(encodeBytes);

        System.out.println(encrypt);


    } catch (Exception e) {
        e.printStackTrace();
    }
    //decrypt
    try
    {
        //abc = Base64.getDecoder().decode(encrypt);
        abc = hexStringToByteArray(encrypt);
        Cipher c = Cipher.getInstance("ECIES","BC");
        c.init(Cipher.DECRYPT_MODE,privateKey);
        decodeBytes = c.doFinal(abc);
        String deCrypt = new String(decodeBytes,"UTF-8");

        System.out.println("Decrypt:"+ deCrypt +"\n");
    }
    catch (Exception ex)
    {
        ex.printStackTrace();
    }
public static String bytesToHex(byte[] bytes) 
{
    char[] hexChars = new char[bytes.length * 2];
    for ( int j = 0; j < bytes.length; j++ ) 
    {
        int v = bytes[j] & 0xFF;
        hexChars[j * 2] = hexArray[v >>> 4];
        hexChars[j * 2 + 1] = hexArray[v & 0x0F];
    }
    return new String(hexChars);
}
public static byte[] hexStringToByteArray(String s) {
    int len = s.length();
    byte[] data = new byte[len / 2];
    for (int i = 0; i < len; i += 2) {
        data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                             + Character.digit(s.charAt(i+1), 16));
    }
    return data;
}
