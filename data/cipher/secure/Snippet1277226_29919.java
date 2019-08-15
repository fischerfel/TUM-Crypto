  //get instance of cipher using BouncyCastle cryptography provider 
  Cipher cipher = Cipher.getInstance( "RSA/ECB/PKCS1Padding", "BC"); 

  //initialize the cipher with the public key pulled from the X509 certificate 
  cipher.init(Cipher.ENCRYPT_MODE, publicKey); 
