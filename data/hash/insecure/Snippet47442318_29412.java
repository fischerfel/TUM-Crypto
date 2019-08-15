`String My_Message = "This is Sample Text";

//Get Key from file tkt_privkey_rsa.pem
PrivateKey priv = loadPrivateKey();

// Compute digest
MessageDigest sha1 = MessageDigest.getInstance("SHA1");
byte[] digest = sha1.digest(My_Message.getBytes());

//Prepare signature.
Signature sign = Signature.getInstance("SHA1withRSA");
sign.initSign(priv);            
sign.update(digest);

//Sign the data with private key.
byte[] realSig = sign.sign();

//encode Signature
String encodedSig = Base64.getEncoder().encodeToString(realSig);

System.out.println("Signature Generated -\n"+encodedSig);

return encodedSig; 
