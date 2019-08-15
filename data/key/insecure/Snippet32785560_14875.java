public String encrypt(String a,int x) {


     String ret="";
  try {

     String text = a;

     String key = "Bar12345Bar12345"; // 128 bit key
     // Create key and cipher
     Key aesKey = new SecretKeySpec(key.getBytes(), "AES");

     Cipher cipher = Cipher.getInstance("AES");
     // encrypt the text
if(x==0)//x==0 means i want to encrypt
{
         cipher.init(Cipher.ENCRYPT_MODE, aesKey);
         byte[] encrypted = cipher.doFinal(text.getBytes());
         ret =new String(encrypted);
}
else//if not 0 i want to decrypt
{
        // decrypt the text

    byte[] encrypted = text.getBytes(Charset.forName("UTF-8"));
         cipher.init(Cipher.DECRYPT_MODE, aesKey);
         String decrypted = new String(cipher.doFinal(encrypted));
         ret=decrypted;
}
  }catch(Exception e) {
     e.printStackTrace();
  }
  return ret;
}
