        private void encript(byte[] data, byte[] clear) {

        byte[] keyBytes = new byte[] { 0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09,
        0x0a, 0x0b, 0x0c, 0x0d, 0x0e, 0x0f, 0x10, 0x11, 0x12, 0x13, 0x14, 0x15, 0x16, 0x17 };
        SecretKeySpec key = new SecretKeySpec(keyBytes, "AES");
        Cipher cipher = null;

        try {

        cipher = Cipher.getInstance("AES/ECB/PKCS7Padding", "BC");

        }    catch (NoSuchAlgorithmException e) {

        e.printStackTrace();
        }    catch (NoSuchProviderException e) {

        e.printStackTrace();

        } catch (NoSuchPaddingException e) {

        e.printStackTrace();
        }
        try {
        cipher.init(Cipher.ENCRYPT_MODE, key);

        } catch (InvalidKeyException e) {

        // TODO Auto-generated catch block
        e.printStackTrace();
        }

        byte[] cipherText = new byte[cipher.getOutputSize(data.length)];

       // int ctLength = cipher.update(data, 0, data.length, cipherText, 0);

        byte[] input = null;

        try { 
        input = cipher.doFinal(cipherText);
        } catch (IllegalBlockSizeException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
        } catch (BadPaddingException e) {
        // TODO Auto-generated catch block


        e.printStackTrace();
        }
        try{
        if(input.length>0){
        Toast.makeText(getApplicationContext(), "Image successfully encripted", 3000).show();
        }
        ByteArrayInputStream bis = new ByteArrayInputStream(input);

        ObjectInputStream ois = new ObjectInputStream(bis);

     File encriptedfile = (File) ois.readObject();//This is the file which i want to Move to App folder

            bis.close();
        ois.close();
        } catch (StreamCorruptedException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
       } catch (IOException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
       } catch (ClassNotFoundException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
       }
    return;
    }
     //Code for copy  image into app folder......
    File outputFolder = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + ".map");
         try {
          FileUtils.copyFile (encriptedfile, outputFolder);
          } 
          catch (IOException e) {
          Log.e("photomover", e.toString());
          }
}
