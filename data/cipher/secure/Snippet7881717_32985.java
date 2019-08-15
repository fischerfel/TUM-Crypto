byte[] plaintext="some text".getBytes();
byte[] encryptedtext;
Cipher cipher=Cipher.getInstance("RSA");
RSAPublicKey pubKey=new RSAPublicKey(????????);
cipher.init(Cipher.ENCRYPT_MODE,pubkey);
cipher.doFinal(plaintext,0,plaintext.length,encryptedtext,0)
