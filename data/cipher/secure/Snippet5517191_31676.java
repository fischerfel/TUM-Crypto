    byte[] ivar = new byte[]
                      {
                              0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09,0x0a, 0x0b, 0x0c, 0x0d, 0x0e, 0x0f
                      };

    ...
    AlgorithmParameterSpec params = new IvParameterSpec(ivar);

    encipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
    encipher .init(Cipher.ENCRYPT_MODE, key, params);

...

public void writeTo(final OutputStream out) throws IOException {
              if (out == null) {
                  throw new IllegalArgumentException("Output stream may not be null");
              }
              try {

              out = new CipherOutputStream(out, encipher );


              int numRead = 0;
              while ( (count = in.read(buf)) !=-1) {
                out.write(buf, 0, count);


              }
              out.flush();
              in.close();

            }
            catch (java.io.IOException e) {
            }
          }
