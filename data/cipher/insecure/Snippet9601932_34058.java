        public static String DecryptBlowfish(byte[] msg){
      byte[] decrypted =null;
      try {


       Cipher cipher = Cipher.getInstance("Blowfish");

          cipher.init(Cipher.DECRYPT_MODE, secretkey);


     decrypted = cipher.doFinal(msg);

        } catch (){ //NoSuchAlgorithmException, NoSuchPaddingException..etc
     }  

    return decrypted;
}
