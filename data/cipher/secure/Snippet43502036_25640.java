 public  String encryptAsymmetric(String input, Key key) throws GeneralSecurityException, IOException {
    byte[] crypted = null;
    try{
        byte[] bytes = input.getBytes("UTF-8");
        //String text = new String(bytes, "UTF-8");
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.ENCRYPT_MODE, key);
        crypted = cipher.doFinal(bytes);
    }catch(Exception e){
        System.out.println(e.toString());
    }//Base64.encodeBase64(crypted)

    return new String(Base64.encode(crypted, Base64.DEFAULT));
}
public  String decryptAsymmetric(String input, Key key){
    byte[] output = null;
    try{

        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.DECRYPT_MODE, key);//Base64.decodeBase64(input.getBytes())
        output = cipher.doFinal(Base64.decode(input.getBytes("UTf-8"), Base64.DEFAULT));
    }catch(Exception e){
        System.out.println(e.toString());
    }
    return new String(output);
}
