public static String encryptAsymmetric(String input, Key key){
      byte[] crypted = null;

      try{

          Cipher cipher = Cipher.getInstance("RSA");
          cipher.init(Cipher.ENCRYPT_MODE, key);
          crypted = cipher.doFinal(input.getBytes());
        }catch(Exception e){
            System.out.println(e.toString());
        }//Base64.encodeBase64(crypted)
        return new String(Base64.getEncoder().encode(crypted));
    }
public static String decryptAsymmetric(String input, Key key){
    byte[] output = null;
    try{

      Cipher cipher = Cipher.getInstance("RSA");
      cipher.init(Cipher.DECRYPT_MODE, key);//Base64.decodeBase64(input.getBytes())
      output = cipher.doFinal(Base64.getDecoder().decode(input.getBytes()));
    }catch(Exception e){
      System.out.println(e.toString());
    }
    return new String(output);
}
