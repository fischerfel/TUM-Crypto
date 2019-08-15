private void encryptFile(String FileName) throws NoSuchAlgorithmException,
               NoSuchPaddingException, InvalidKeyException, IOException, IllegalBlockSizeException, BadPaddingException, DbxException {
        // TODO Auto-generated method stub
        String key = "Mary has one cat";

           // char[] hex = encodeHex(SecKey.getEncoded());
            //String key = String.valueOf(hex);
            System.out.println(key);
            Key secretKey = new SecretKeySpec(key.getBytes(), ALGORITHM);
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            FileInputStream inputStream = new FileInputStream(FileName);
            byte[] inputBytes = new byte[(int) FileName.length()];
            inputStream.read(inputBytes);

            byte[] outputBytes = cipher.doFinal(inputBytes);

            FileOutputStream outputStream = new FileOutputStream(FileName);
            outputStream.write(outputBytes);
            inputStream.close();
            outputStream.close();
            uploadToDropbox(FileName);

    }


public void uploadToDropbox(String fileName) throws DbxException,
        IOException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {

    FileInputStream fis = new FileInputStream(fileName);
    try {
        DbxEntry.File uploadedFile = dbxClient.uploadFile("/" + fileName,
                DbxWriteMode.add(), fileName.length(), fis);
        String sharedUrl = dbxClient.createShareableUrl("/" + fis);
        System.out.println("Uploaded: " + uploadedFile.toString() + " URL "
                + sharedUrl);
    } finally {
        fis.close();

    }
}
