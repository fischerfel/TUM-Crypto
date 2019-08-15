private SecretKey symmKey = null;
public String encrypt(String strToEncrypt) throws  Exception
 {

     String symmEncryptMode = "DESede";
     String encString= null;

     KeyGenerator keyGen = KeyGenerator.getInstance(symmEncryptMode);
     symmKey = keyGen.generateKey();

     byte dataToEncrypt[] = strToEncrypt.getBytes();
     Cipher symmCipher = Cipher.getInstance(symmEncryptMode);
     symmCipher.init(Cipher.ENCRYPT_MODE, symmKey);
     byte[] encrypted  = symmCipher.doFinal(dataToEncrypt);
     encString= new String(Base64.encode(encrypted));
     encString = URLEncoder.encode(encString, "UTF-8");
     return(encString);
} //end method create Signature
