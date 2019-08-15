byte[] keyBytes = key.getBytes("UTF-8");
byte[] ivBytes = ivString.getBytes("UTF-8");


public static  void  decryptPDFBook(String filePath, String key, String ivString, ShelfItem item) {

    InputStream  finStream;
    byte[] keyBytes = key.getBytes();
    byte[] ivBytes = ivString.getBytes();

    SecretKeySpec secretKeySpec = new SecretKeySpec(keyBytes, "AES");
    IvParameterSpec ivParamSpec = new IvParameterSpec(ivBytes);
    try {
        finStream = new FileInputStream(new File(filePath));
        Cipher cipherInstance = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipherInstance.init(Cipher.DECRYPT_MODE, secretKeySpec, ivParamSpec);
        CipherInputStream cipherInputStream = new CipherInputStream(finStream, cipherInstance);
        ByteArrayOutputStream byteArrayOS = new ByteArrayOutputStream();

        String newpath= Environment.getExternalStorageDirectory() +File.separator+ "ebooks"+File.separator+"ief"+File.separator+item.getId()+"_temp.pdf";

        File dir = new File(Environment.getExternalStorageDirectory() +File.separator+ "ebooks"+File.separator+"ief");
        if (!dir.exists()) {
            dir.mkdirs();
        }
        FileOutputStream fos = new FileOutputStream(newpath);

        byte[] decodedByteChunk = new byte[1024];   
        int bytesAvailable = cipherInputStream.read(decodedByteChunk);
        while (bytesAvailable != -1) {
            fos.write(decodedByteChunk);
            bytesAvailable = cipherInputStream.read(decodedByteChunk);
        }
        byteArrayOS.close();
        cipherInputStream.close();
        fos.close();

       File encryptedFile = new File(filePath);
        if(encryptedFile.exists()){
           encryptedFile.delete();
           File file = new File (newpath);
           file.renameTo(encryptedFile);
       }


    } catch (NoSuchPaddingException nspe) {
        System.out.println("Inside NoSuchPaddingException");
        //Log.d("ELSAPAC", "BookExtractionUtil decryptPDFBook NoSuchPaddingException");
        nspe.printStackTrace();
    } catch (NoSuchAlgorithmException nsae) {
        System.out.println("Inside NoSuchAlgorithmException");
        //Log.d("ELSAPAC", "BookExtractionUtil decryptPDFBook NoSuchAlgorithmException");
        nsae.printStackTrace();
    } catch (InvalidKeyException ike) {
        System.out.println("Inside InvalidKeyException");
        //Log.d("ELSAPAC", "BookExtractionUtil decryptPDFBook InvalidKeyException");
        ike.printStackTrace();
    } catch (InvalidAlgorithmParameterException iape) {
        System.out.println("Inside InvalidAlgorithmParameterException");
        //Log.d("ELSAPAC", "BookExtractionUtil decryptPDFBook InvalidAlgorithmParameterException");
        iape.printStackTrace();
    } catch (IOException ioe) {     
        System.out.println("Inside IOException");
        //Log.d("ELSAPAC", "BookExtractionUtil decryptPDFBook IOException");
        ioe.printStackTrace();
        decryptPDFWithoutPadding( filePath,key,ivString,item);//pad block corrupted
        //return data;
    } catch (Exception e) {
       e.printStackTrace();
    }
     finally {        
    }

   // return null;

}
