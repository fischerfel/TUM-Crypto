public static String decrypt(String encryptStr){
                    String decrypted = null;
        try {

            while(encryptStr != null){
                try
                {

                    String key = "Bar12345Bar12345"; // 128 bit key
                    // Create key and cipher
                    Key aesKey = new SecretKeySpec(key.getBytes(), "AES");
                    Cipher cipher = Cipher.getInstance("AES");
                    // encrypt the text
                    cipher.init(Cipher.ENCRYPT_MODE, aesKey);

                    // for decryption
                    byte[] bb = new byte[encryptStr.length()];
                    for (int i=0; i<encryptStr.length(); i++) {
                        bb[i] = (byte) encryptStr.charAt(i);
                    }

                    // decrypt the text
                    cipher.init(Cipher.DECRYPT_MODE, aesKey);
                    decrypted = new String(cipher.doFinal(bb));

                }
                catch(Exception e)
                {
                    e.printStackTrace();
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(Snake_H.class.getName()).log(Level.SEVERE, null, ex);
        }
        return decrypted;
    }
