String getDecodedString(String key,String encodedValue,SupportedEncryptionAlgorithm algoInfo) 
{
    Cipher cipher = getCipherInstancenew(algoInfo, key,Cipher.DECRYPT_MODE);
    try
    {
        byte[] dec = new sun.misc.BASE64Decoder().decodeBuffer(encodedValue);

        int ctLength = cipher.getOutputSize(dec.length);
        byte[] plainText = new byte[cipher.getOutputSize(ctLength)];

        int ptLength = cipher.update(dec, 0, ctLength, plainText, 0);
        ptLength += cipher.doFinal(plainText, ptLength);

        return null; 
    }
    catch (IllegalBlockSizeException e)
    {
        LoggerFactory.getLogger(EncryptionHelper.class).error("Security Alert",e);
    }
    catch (BadPaddingException e)
    {
        LoggerFactory.getLogger(EncryptionHelper.class).error("Security Alert",e);
    }
    return null;
}

public static byte[] stringToBytes(String s) {
    byte[] b2 = new BigInteger(s, 36).toByteArray();
    return Arrays.copyOfRange(b2, 1, b2.length);
}

public static Cipher getCipherInstancenew(SupportedEncryptionAlgorithm algoInfo,String keyString,int mode) throws IOException
{
    byte[] decodedBytes;
    Cipher cipher=null;      
    try
    {
        decodedBytes = getBase64FromHEX(keyString).getBytes();
        SecretKeySpec skeySpec = new SecretKeySpec(decodedBytes, "AES");
        Security.addProvider(new BouncyCastleProvider());   
        cipher = Cipher.getInstance("AES/ECB/PKCS5Padding", "BC");
        cipher.init(mode, skeySpec );
    }
    catch (java.security.GeneralSecurityException e)
    {
        /*Strictly no logging as it is security class 
         *  There seems to be some issue with the Keys so alert it */
         //LoggerFactory.getLogger(EncryptionHelper.class).error("Security Alert",e);
         throw new IOException("GetCipherInstance does not exsists");
    }

    return cipher;

}

public static String getBase64FromHEX(String input) {

    byte barr[] = new byte[16];
    int bcnt = 0;
    for (int i = 0; i < 32; i += 2) {
        char c1 = input.charAt(i);
        char c2 = input.charAt(i + 1);
        int i1 = intFromChar(c1);
        int i2 = intFromChar(c2);

        barr[bcnt] = 0;
        barr[bcnt] |= (byte) ((i1 & 0x0F) << 4);
        barr[bcnt] |= (byte) (i2 & 0x0F);
        bcnt++;
    }

    BASE64Encoder encoder = new BASE64Encoder();
    return encoder.encode(barr);
}

private static int intFromChar(char c) {
    char[] carr = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

    char clower = Character.toLowerCase(c);
    for (int i = 0; i < carr.length; i++) {
        if (clower == carr[i]) {
            return i;
        }
    }

    return 0;
}
