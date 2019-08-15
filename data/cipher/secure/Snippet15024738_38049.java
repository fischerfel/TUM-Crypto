public String Decrypt(String text) throws Exception
{
    try{
        Log.i("Crypto.java:Decrypt", text);
        RSAPrivateKey privateKey = (RSAPrivateKey)kp.getPrivate();
        Cipher cipher = Cipher.getInstance("RSA/ECB/NoPadding");
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            byte[] cipherData = cipher.doFinal(text.getBytes());// <----ERROR: too much data for RSA block
            byte[] decryptedBytes = cipher.doFinal(cipherData);
            String decrypted = new String(decryptedBytes);

            Log.i("Decrypted", decrypted);

        return decrypted;
    }catch(Exception e){
        System.out.println(e.getMessage());
    }
    return null;
}
