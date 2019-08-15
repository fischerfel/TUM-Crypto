private static byte[] DESEdeDecrypt(byte[] keyBytes, byte[] dataBytes){

    byte[] decryptedData = null;
    try{
        DESedeKeySpec keySpec = new DESedeKeySpec(keyBytes, 0); 
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DESede");
        SecretKey key = keyFactory.generateSecret(keySpec); 
        Cipher cipher = Cipher.getInstance("DESede");
        cipher.init(Cipher.DECRYPT_MODE, key);
        decryptedData = cipher.doFinal(dataBytes);
    }
    catch(Exception e){System.out.println(e);}  

    return decryptedData;
