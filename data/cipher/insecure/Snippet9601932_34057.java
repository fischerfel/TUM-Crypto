    public static byte[] EncryptBlowfish(String msg){

    byte[] encrypted =null;

    try {


    Cipher cipher = Cipher.getInstance("Blowfish");

    cipher.init(Cipher.ENCRYPT_MODE, secretkey);

    encrypted = cipher.doFinal(msg.getBytes());

    } catch (){ //NoSuchAlgorithmException, NoSuchPaddingException..etc
   }  

    return encrypted;
}
