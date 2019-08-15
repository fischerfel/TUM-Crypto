KeyAgreement keyAgree = KeyAgreement.getInstance("DH", "BC");
keyAgree.init(this.smartphonePrivKey);
keyAgree.doPhase(serverPubKey, true);
SecretKey key = keyAgree.generateSecret("AES");
System.out.println("Key Length: " + key.getEncoded().length);
System.out.println("Key Algorithm: "+ key.getAlgorithm());
System.out.println("Key Format: "+ key.getFormat());

byte[] encrypted = null;
  Cipher cipher;
  try {
   cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
   System.out.println("Allowed Key Length: "
     + cipher.getMaxAllowedKeyLength("AES"));
   cipher.init(Cipher.ENCRYPT_MODE, key);
   encrypted = cipher.doFinal("YEAH".getBytes("UTF8"));
  } catch (NoSuchAlgorithmException e) {
   e.printStackTrace();
  } catch (NoSuchPaddingException e) {
   e.printStackTrace();
  } catch (InvalidKeyException e) {
   e.printStackTrace();
  } catch (IllegalBlockSizeException e) {
   e.printStackTrace();
  } catch (BadPaddingException e) {
   e.printStackTrace();
  } catch (UnsupportedEncodingException e) {
   e.printStackTrace();
  }
