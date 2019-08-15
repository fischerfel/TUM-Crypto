 File uploadedFile = new File(filePath);
 item.write(uploadedFile);

        //Convert uploaded image to byte[] for encryption
         bFile = new byte[(int) uploadedFile.length()];

        try {
         FileInputStream fileInputStream = new FileInputStream(uploadedFile);
         //convert file into array of bytes
         fileInputStream.read(bFile);
         fileInputStream.close();
        } catch (Exception e) {
         e.printStackTrace();
        }
      //encrypt the byte array

       Cipher cipher=Cipher.getInstance("AES");
       cipher.init(Cipher.ENCRYPT_MODE, key);
       enFile=cipher.doFinal(bFile);
       //save encrypted image to database
       image.setImage(enFile);
       //save this row to database
       UploadService ups=new UploadService();
       imgid=ups.uploadImage(image);


        //Attach bitmap header to the encrypted byte array so that encrypted array can be still recognized as an image and save it to disk
        BitmapEncoder.encodeToBitmap(enFile, new File(uploadFolder + File.separator +"encrypted"+fileName));
