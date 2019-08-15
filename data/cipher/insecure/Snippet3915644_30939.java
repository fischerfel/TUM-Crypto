         String bytes = toHex("the 16 bit key");
            Key skeySpec = new SecretKeySpec(toByte(bytes), "AES");
            Cipher c = Cipher.getInstance("AES/ECB/PKCS7Padding");  

            byte[] buf = new byte[1024]; 


            // Bytes read from in will be decrypted 

            InputStream inCipher = new FileInputStream(enc_File);
            OutputStream outCipher = new FileOutputStream(cipherFile);
            c.init(Cipher.DECRYPT_MODE, skeySpec);
            inCipher = new CipherInputStream(inCipher, c); // Read in the decrypted bytes and write the cleartext to out 
            int numRead = 0;            

                    while ((numRead = inCipher.read(buf)) >= 0) {
                          outCipher.write(buf, 0, numRead);
                } 
                outCipher.close(); 
