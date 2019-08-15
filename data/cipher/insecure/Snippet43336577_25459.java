public static String decrypt(String strToDecrypt)
{
    char[] ch=strToDecrypt.toCharArray();

    System.out.println("Test: " + ch);
    try
    {

        Cipher cipher2 = Cipher.getInstance("AES/ECB/NoPadding");

        cipher2.init(Cipher.DECRYPT_MODE, secretKey);

        setDecryptedString(new String(cipher2.doFinal(Hex.decodeHex(ch))));            // Output as String eg: testingone


    }
    catch (Exception e)
    {

        System.out.println("Error while decrypting: "+e.toString());
    }
    return null;
}
