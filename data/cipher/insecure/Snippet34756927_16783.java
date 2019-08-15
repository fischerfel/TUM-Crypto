desCipher = Cipher.getInstance("DES/OFB56/NoPadding");
desCipher.init(Cipher.DECRYPT_MODE, secretKey, ivParameter);
for (i=0;i<subframeCount;i++){
// perform the skip iteration here
  if (firstFrame){
      byte[] dummy = new byte[7];
      dummy[0] = 1;dummy[1] = 12;dummy[2] = 12;dummy[3] = 15;dummy[4] = 26;dummy[5] = 12;dummy[6] = 12;
      desCipher.update(dummy);
  }
  if (not_last_frame){
      decryptedVCW = desCipher.update(vcwShift_E);
  }
  else{
      decryptedVCW = desCipher.doFinal(vcwShift_E);
  }

}
