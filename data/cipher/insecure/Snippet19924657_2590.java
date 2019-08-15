  Cipher desCipher;
  KeyGenerator keygenerator;
  SecretKey myDesKey; 
  try{
    keygenerator = KeyGenerator.getInstance("DES");
    myDesKey = keygenerator.generateKey();
    byte[] encoded = myDesKey.getEncoded();
    // convert secret key to string 
   String stringKey =Base64.encodeToString(myDesKey.getEncoded());

    // converting back from  string to secret key. 
    //its returning javax.crypto.spec.SecretKey object but i need com.sun.crypto.provider.DESKey object

   SecretKey originalKey = new SecretKeySpec(stringKey.getBytes(), 0, stringKey.length(), "DES"); 
      String text="hello how are you " 
     desCipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
    desCipher.init(Cipher.ENCRYPT_MODE, myDesKey);
    byte[] textEncrypted = desCipher.doFinal(text.getBytes());   

    System.out.println("text encrypted successfully");        

    }
    catch(Exception ex)
     {
       ex.printStackTrace();
     }

is throwing the exception 
