class Helper{  
 public Cipher dcipher,ecipher;  
 public Helper(String passPhrase){  
    byte[] salt = 
      {  (byte)0xA9, (byte)0x9B, (byte)0xC8, (byte)0x32,  
         (byte)0x56, (byte)0x34, (byte)0xE3, (byte)0x03  
      };  
    int iterationCount = 19;  
    try {    
          KeySpec keySpec = new PBEKeySpec(passPhrase.toCharArray(), salt,                  
                    iterationCount);  
          SecretKey key =                                
              SecretKeyFactory.getInstance("PBEWithMD5AndDES").generateSecret(keySpec);  
          ecipher = Cipher.getInstance(key.getAlgorithm());  
          dcipher = Cipher.getInstance(key.getAlgorithm());  
          AlgorithmParameterSpec paramSpec = new PBEParameterSpec(salt,
                                       iterationCount);  
          ecipher.init(Cipher.ENCRYPT_MODE, key, paramSpec);  
          dcipher.init(Cipher.DECRYPT_MODE, key, paramSpec);  
    } 
    catch(Exception e){ } 
}  
@SuppressWarnings("unused")  
protected String encrypt(String str){  
        try{  
          byte[] utf8 = str.getBytes("UTF8");  
          byte[] enc = ecipher.doFinal(utf8);  
          return new sun.misc.BASE64Encoder().encode(enc);  
        } 
        catch (Exception e) {   } 
        return null;  
}      
// Decrpt password     
//To decrypt the encryted password  
protected String decrypt(String str) {  
  Cipher dcipher = null;  
  try{  
        byte[] salt = {(byte)0xA9, (byte)0x9B, (byte)0xC8, (byte)0x32,(byte)0x56, 
                       (byte)0x34, (byte)0xE3, (byte)0x03};  
        int iterationCount = 19;  
      try{   
        String passPhrase="";  
        KeySpec keySpec = new PBEKeySpec(passPhrase.toCharArray(), salt,
                        iterationCount);  
        SecretKey key = 
              SecretKeyFactory.getInstance("PBEWithMD5AndDES").generateSecret(keySpec);            
        dcipher = Cipher.getInstance(key.getAlgorithm());  
        // Prepare the parameters to the cipthers   
            AlgorithmParameterSpec paramSpec = new PBEParameterSpec(salt, 
                               iterationCount);  
        dcipher.init(Cipher.DECRYPT_MODE, key, paramSpec);  
      }   
  catch (Exception e) { 
            System.out.println("EXCEPTION: InvalidAlgorithmParameterException");  
      }  
      byte[] dec = new sun.misc.BASE64Decoder().decodeBuffer(str);  
      // Decrypt  
      byte[] utf8 = dcipher.doFinal(dec);  
      // Decode using utf-8  
      return new String(utf8, "UTF8");  
   }  
   catch (BadPaddingException e) {  
   } catch (IllegalBlockSizeException e) {  
   } catch (UnsupportedEncodingException e) {  
   } catch (IOException e){  
}  
return null;  
}  
