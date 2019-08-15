public  String encryptAsymmetric(String input, Key key) throws GeneralSecurityException, IOException {
    byte[] crypted = null;
    try{

        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, key);
        crypted = cipher.doFinal(input.getBytes());
    }catch(Exception e){
        System.out.println(e.toString());
    }//Base64.encodeBase64(crypted)

    return new String(Base64.encode(crypted, Base64.DEFAULT));
}
public  String decryptAsymmetric(String input, Key key){
    byte[] output = null;
    try{

        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, key);//Base64.decodeBase64(input.getBytes())
        output = cipher.doFinal(Base64.decode(input.getBytes(), Base64.DEFAULT));
    }catch(Exception e){
        System.out.println(e.toString());
    }
    return new String(output);
}
