// Here is the sample java code:
String inputString ="have a good day"; 

   final Cipher encryptCipher = Cipher.getInstance("AES");                          
            encryptCipher.init(Cipher.ENCRYPT_MODE,   generateMySQLAESKey("default", "UTF-8"));     


            String encryptedText =new String(Hex.encodeHex(encryptCipher.doFinal(inputString.getBytes("UTF-8"))));

    Deflater deflater = new Deflater();
    deflater.setInput(encryptedText );
    ByteArrayOutputStream outputStream = new             ByteArrayOutputStream(data.length);  
    deflater.finish();
    byte[] buffer = new byte[1024];
    while (!deflater.finished()) {
        int count = deflater.deflate(buffer); // returns the generated
                                                // code... index
        outputStream.write(buffer, 0, count);
    }
    outputStream.close();
    byte[] output = outputStream.toByteArray();

    return Base64.encodeBase64String(output);


 //I want it to match mysql query like:

select TO_BASE64(COMPRESS(HEX(AES_ENCRYPT("have a good day",default))) from dual;
