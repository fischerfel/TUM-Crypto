Cipher cipher;
SecretKey key;
byte[] buf = new byte[1024];
DesEncrypter() {
   byte[] iv = new byte[] { 0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09,
      0x0a, 0x0b, 0x0c, 0x0d, 0x0e, 0x0f, 0x10, 0x11, 0x12, 0x13, 0x14, 0x15, 0x16, 0x17 };
   key = new SecretKeySpec(iv, "AES");
   try {
     cipher = Cipher.getInstance("AES/ECB/PKCS7Padding", "BC");
     cipher.init(Cipher.ENCRYPT_MODE, key);          
   } 
   catch (NoSuchProviderException e) {}
 }        

public void encrypt(InputStream in, OutputStream out) {
    try {
      out = new CipherOutputStream(out, cipher);
      int numRead = 0;
      while ((numRead = in.read(buf)) >= 0) {
          out.write(buf, 0, numRead);
      }
      out.close();
    } 
