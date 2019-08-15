static {
       Security.insertProviderAt(new BouncyCastleProvider(), 1);
      }

//...more code here

byte[] pka = Base64.decode(base64key);

X509EncodedKeySpec x = new X509EncodedKeySpec(pka);
PublicKey publicKey = KeyFactory.getInstance("RSA").generatePublic(x);

byte[] testToByte = "test".getBytes("UTF8"); 

KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA"); 
keyGen.initialize(2048); 

Cipher cipher = Cipher.getInstance("RSA"); 
cipher.init(Cipher.ENCRYPT_MODE, publicKey); 

byte[] cipherText = cipher.doFinal(testToByte); 

String encrypted = Base64.encode((new String(cipherText, "UTF8").toString().getBytes()))
