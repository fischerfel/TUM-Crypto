 //encrypt using blowfish algorithm
    public static byte[] encrypt(String Data)throws Exception{

        SecretKeySpec key = new SecretKeySpec(strkey.getBytes("UTF8"), "Blowfish");
         Cipher cipher = Cipher.getInstance("Blowfish");
         cipher.init(Cipher.ENCRYPT_MODE, key);

         return (cipher.doFinal(Data.getBytes("UTF8")));

    }

    //decrypt using blow fish algorithm
    public static String decrypt(byte[] encryptedData)throws Exception{
         SecretKeySpec key = new SecretKeySpec(strkey.getBytes("UTF8"), "Blowfish");
         Cipher cipher = Cipher.getInstance("Blowfish");
         cipher.init(Cipher.DECRYPT_MODE, key);
         byte[] decrypted = cipher.doFinal(encryptedData);
         return new String(decrypted); 

    }
