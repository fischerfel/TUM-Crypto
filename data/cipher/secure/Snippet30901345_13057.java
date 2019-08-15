public String decryptString(String encryptedVal, String sessionId)
{

    String sResult="";
    try
    {

        PrivateKey pvtKey = getPrivateKeyFromSession(sessionId);
        Cipher pkCipher=null;

        pkCipher = Cipher.getInstance("RSA/None/NoPadding", "BC");
        pkCipher.init(Cipher.DECRYPT_MODE, pvtKey);
        Base64 encoder = Base64.getInstance();          
        byte[] decodedValue = encoder.decode(encryptedVal);
        byte[] deCryptedBytes =pkCipher.doFinal( decodedValue );
        sResult = new String(deCryptedBytes);
    }           
    catch(Exception ex){log.error("Error decryptString" + ex.getMessage()); }
    return sResult.trim();
}
