public static String encrypt_cbc(String input, String key, String iv){
    byte[] crypted = null;
    try{
         //byte[] key_byte = Hex.decodeHex(key.toCharArray());
         byte[] key_byte = DatatypeConverter.parseHexBinary(key); 
         SecretKeySpec key_spec = new SecretKeySpec(key_byte, "AES");

         //byte[] iv_byte = Hex.decodeHex(iv.toCharArray());
         byte[] iv_byte = DatatypeConverter.parseHexBinary(IV); 
         IvParameterSpec iv_spec = new IvParameterSpec(iv_byte);

         Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");

         cipher.init(Cipher.ENCRYPT_MODE, key_spec, iv_spec);
         crypted = cipher.doFinal(input.getBytes());

    }catch(Exception e){
         System.out.println(e.toString());
    }
    String crypted_hex = Hex.encodeHexString(crypted);
    return crypted_hex;
}
