java.security.PrivateKey privatekey = KeyFactory.getInstance("RSA").generatePrivate(new    
PKCS8EncodedKeySpec(PRIVATE_RSA_KEY));//PRIVATE_RSA_KEY is a byte array
cipher = Cipher.getInstance("RSA/None/PKCS1Padding");
cipher.init(2, privatekey);
(...)
cipher.doFinal(examPrivateKey, k, l); //k and l are integers
