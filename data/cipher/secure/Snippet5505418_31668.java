  KeyGenerator kgen = KeyGenerator.getInstance("AES");
          kgen.init(128);
          key = kgen.generateKey();

    byte[] ivar = new byte[]
                      {
                          0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09,0x0a, 0x0b, 0x0c, 0x0d, 0x0e, 0x0f
                  };
AlgorithmParameterSpec params = new IvParameterSpec(ivar );
dcipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
dcipher.init(Cipher.DECRYPT_MODE, key, params );
