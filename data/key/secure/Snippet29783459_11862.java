   try {
   FileOutputStream outStream;
   CipherOutputStream cos;
   try (FileInputStream file = new FileInputStream(pOld.toString())) {
       outStream = new FileOutputStream(pNew.toString());
       byte k[]= Key.getBytes();
       SecretKeySpec KEYY=new SecretKeySpec(k, EncAlgo);
       Cipher enc = Cipher.getInstance(EncAlgo);
       enc.init(Cipher.DECRYPT_MODE, KEYY);
       cos = new CipherOutputStream(outStream,enc);
       byte [] buffer = new byte[1024];
       int read;
       while((read=file.read(buffer))!=-1)
       {

          cos.write(buffer, 0, read);
       }
   }
        outStream.flush();
        cos.close();
        JOptionPane.showMessageDialog(null, "The Message Was Decrypted Successfully");           
    }
 catch(HeadlessException | IOException | InvalidKeyException |      NoSuchAlgorithmException | NoSuchPaddingException e) {
JOptionPane.showMessageDialog(null, "Decryption Failed");
 }
