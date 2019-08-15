public void mytestSimple(long code, String password) throws Exception 
       { SecretKey key       = new SecretKeySpec (password.getBytes(),"DES");
         Cipher    ecipher   = Cipher.getInstance("DES/ECB/PKCS5Padding");
         byte[]    plaintext = new byte[8];

         for (int i=0; i<8; i++)
             { plaintext[7-i] = (byte) (code & 0x00FF);
                >>>= 8;
             }

         ecipher.init      (Cipher.ENCRYPT_MODE, key);
         System.out.println(ecipher.getOutputSize(8));

         byte[] encrypted = ecipher.doFinal(plaintext);
         System.out.println("--" + encrypted.length);

         Cipher dcipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
         dcipher.init(Cipher.DECRYPT_MODE, key);

         byte[] crypttext = dcipher.doFinal(encrypted);
         long   decoded    = 0;

         for (int i=0; i<8; i++)
             { decoded <<= 8;
               decoded  += crypttext[i] & 0x00FF;
             }

         System.out.println(decode + "--" + crypttext.length);
       }
