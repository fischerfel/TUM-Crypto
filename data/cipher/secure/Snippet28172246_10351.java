Cipher с=Cipher.getInstance("AES/CBC/PKCS7Padding");
с.init(Cipher.DECRYPT_MODE, key, iv);
....

void Decode(Cipher c)
{
   c.doFinal();//reset cipher !?
   //Start a new decoding session
   ....
}
