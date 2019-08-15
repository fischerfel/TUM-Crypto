public static byte[] decrypt(byte[] inputbuffer) throws Exception {
    Key key = new SecretKeySpec(keybyte, "AES");
    IvParameterSpec ivSpec = new IvParameterSpec(iv);
    Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding");
    c.init(Cipher.DECRYPT_MODE, key, ivSpec);

    c.getBlockSize();
    System.out.println("Block size="+c.getBlockSize());

    int outlen = c.getOutputSize(inputbuffer.length);
    System.out.println("Output length will be:"+outlen);
    byte[] result=c.doFinal(inputbuffer);
    return result;
}

  public static byte[] decodeBase64(String data) throws IOException{ 
    BASE64Decoder decoder = new BASE64Decoder(); 
    byte[] decodedBytes = decoder.decodeBuffer(data);

    return decodedBytes;
  }


   public static void unzipPrint(byte[] data) throws Exception{
    InputStream is=new GZIPInputStream(new ByteArrayInputStream(data));
    int ch2;
    while((ch2=is.read())!=-1) {
        System.out.print((char)ch2);
    }
   }
