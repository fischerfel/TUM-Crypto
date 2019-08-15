EncryptionParameter encryptionParameter = new EncryptionParameter ();
encryptionParameter.setEncrParameter(reciver , message);
// the reciverand the message are both String

// I created  a class to convert an object to byte[]  called ObjectToByte
ObjectToByte otp = new ObjectToByte();
byte[] MByte = otp.convert(encryptionParameter); // convert the object to byte[]
//  Encrypt the message 
Cipher c = Cipher.getInstance("RSA");
c.init(Cipher.ENCRYPT_MODE,reciverKey); 
byte[] myEncryptMessage = c.doFinal(MByte);
