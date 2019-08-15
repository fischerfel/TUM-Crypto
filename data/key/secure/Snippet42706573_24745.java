//generate a random HMAC
Key key = MacProvider.generateKey(SignatureAlgorithm.HS256);

//Get the key data
byte keyData[]= key.getEncoded();
//Store data in a file...

//Build key
Key key = new SecretKeySpec(keyData, SignatureAlgorithm.HS256.getJcaName());
