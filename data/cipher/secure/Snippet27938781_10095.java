import (same as code above)
...
  Security.addProvider(new FlexiCoreProvider());
  Security.addProvider(new FlexiECProvider());
  Cipher cipher = Cipher.getInstance("ECIES","FlexiEC");
  IESParameterSpec iesParams = new IESParameterSpec ("AES128_CBC","HmacSHA1", null, null);

  byte[] decodedPubKey = Base64.decode(publicKey);

  X509EncodedKeySpec formatted_public = new X509EncodedKeySpec(decodedPubKey);
  KeyFactory kf = KeyFactory.getInstance("ECIES","FlexiEC");

  PublicKey publicKey = kf.generatePublic(formatted_public); // <--- I got error when hit this row

  cipher.init(Cipher.ENCRYPT_MODE, publicKey, iesParams);

  byte[] block = "this my message".getBytes();
  System.out.println("Plaintext: "+ new String(block));

  byte [] Ciphered = cipher.doFinal(block);
  System.out.println("Ciphertext : "+ Base64.encodeBytes(Ciphered));
