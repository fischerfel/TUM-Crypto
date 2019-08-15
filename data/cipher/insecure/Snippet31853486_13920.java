private void encrypt_btnActionPerformed(java.awt.event.ActionEvent evt) {                                            
  try{
      Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
      input = entered_txt.getText().getBytes();
      SecretKeySpec key = new SecretKeySpec(keyBytes, "DES");
      IvParameterSpec ivSpec = new IvParameterSpec(ivBytes);
      cipher = Cipher.getInstance("DES/CTR/NoPadding","BC");

      cipher.init(Cipher.ENCRYPT_MODE, key, ivSpec);
      cipherText = new byte[cipher.getOutputSize(input.length)];

      ctLength+=cipher.update(input, 0, input.length, cipherText, 0);

      ctLength+= cipher.doFinal(cipherText, ctLength);
      encrypted_txt.setText(new  String(cipherText));

  }catch(NoSuchAlgorithmException | NoSuchProviderException | NoSuchPaddingException | InvalidKeyException | InvalidAlgorithmParameterException | ShortBufferException | IllegalBlockSizeException | BadPaddingException e){
      JOptionPane.showMessageDialog(null, e);
      e.printStackTrace();
  }
}
