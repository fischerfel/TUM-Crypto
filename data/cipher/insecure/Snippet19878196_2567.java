public static String encryptBlowFish(String to_encrypt, String salt){
    String dbpassword = null;
    try{
        SecretKeySpec skeySpec = new SecretKeySpec( salt.getBytes(), "Blowfish" );

        // Instantiate the cipher.
        Cipher cipher = Cipher.getInstance("Blowfish/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec);

        //byte[] encrypted = cipher.doFinal( URLEncoder.encode(data).getBytes() );
        byte[] encrypted = cipher.doFinal( to_encrypt.getBytes() );
        dbpassword = new String(encrypted);
    } catch (Exception e) {
        System.out.println("Exception while encrypting");
        e.printStackTrace();
         dbpassword = null;
    } finally {
        return  dbpassword;
    }
}

public static String decryptBlowFish(String to_decrypt, String salt){
    String dbpassword = null;
    try{
        SecretKeySpec skeySpec = new SecretKeySpec( salt.getBytes(), "Blowfish" );

        // Instantiate the cipher.
        Cipher cipher = Cipher.getInstance("Blowfish/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, skeySpec);

        //byte[] encrypted = cipher.doFinal( URLEncoder.encode(data).getBytes() );
        byte[] encrypted = cipher.doFinal( to_decrypt.getBytes() );
        dbpassword = new String(encrypted);
    } catch (Exception e) {
        System.out.println("Exception while decrypting");
        e.printStackTrace();
        dbpassword = null;
    } finally {
        return  dbpassword;
    }
}
