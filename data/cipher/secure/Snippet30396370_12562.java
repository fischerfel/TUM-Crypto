public static []byte RSADecryptB(PrivateKey privatekey, byte[] cipherText) {
byte[] ll = null;
  try {
     /* Create cipher for decryption. */
     Cipher decrypt_cipher = Cipher.getInstance("RSA");
     decrypt_cipher.init(Cipher.DECRYPT_MODE, privatekey);


     ll = decrypt_cipher.doFinal(cipherText);

  } catch (IllegalBlockSizeException e) {
     System.out.println("1");
     e.printStackTrace();
  }
  catch (InvalidKeyException et) {
     System.out.println("2");
     et.printStackTrace();
  }
  catch (NoSuchAlgorithmException ev) {
     System.out.println("3");
     ev.printStackTrace();
  }
  catch (BadPaddingException ea) {
     System.out.println("4");
     ea.printStackTrace();
  }
  catch (NoSuchPaddingException eo) {
     System.out.println("5");
     eo.printStackTrace();
  }
  return ll;
