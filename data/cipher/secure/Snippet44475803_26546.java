PrivateKey privateKey = null;
privateKey = appMob.getPrivKey(keys[1]); //return the privatekey      
System.out.println("\n--- Decryption started--- \n");
byte[] descryptedData = null;

try{
    byte[] decrypt = smsWithCryptedData.getBytes("UTF-8");
    Cipher cipher = Cipher.getInstance("RSA");
    cipher.init(Cipher.DECRYPT_MODE,privateKey);
    descryptedData = cipher.doFinal(decrypt);
    String smsFinal = new String(descryptedData);

    System.out.println("decrypt \n smsFinal");


}catch(Exception e)
    {
        e.printStackTrace();

    }
