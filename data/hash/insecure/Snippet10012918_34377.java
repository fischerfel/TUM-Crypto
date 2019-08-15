static Cipher createCipher(int mode, String password) throws Exception {
            PBEKeySpec keySpec = new PBEKeySpec(password.toCharArray());
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("PBEWithMD5AndDES");
            SecretKey key = keyFactory.generateSecret(keySpec);
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update("input".getBytes());
            byte[] digest = md.digest();
            byte[] salt = new byte[8];
            for (int i = 0; i < 8; ++i)
              salt[i] = digest[i];
            PBEParameterSpec paramSpec = new PBEParameterSpec(salt, 20);
            Cipher cipher = Cipher.getInstance("PBEWithMD5AndDES");
            cipher.init(mode, key, paramSpec);
            return cipher;
    }

     static void applyCipher(String inFile, String outFile, Cipher cipher) throws Exception {
            String decryption = "";
            CipherInputStream in = new CipherInputStream(new FileInputStream(inFile), cipher);
            BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(outFile));
            int BUFFER_SIZE = 8;
            byte[] buffer = new byte[BUFFER_SIZE];
            int numRead = 0;
            do {
              numRead = in.read(buffer);
              System.out.println(buffer + ", 0, " + numRead);
              if (numRead > 0){
                out.write(buffer, 0, numRead);
                System.out.println(toHexString(buffer, 0, numRead));
              }
             } while (numRead == 8);
            in.close();
            out.flush();
            out.close();
          }
     private static char[] hex_table = {
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 
            'a', 'b', 'c', 'd', 'e', 'f'};

     public static String toHexString(byte[] data, int offset, int length)
     {
       StringBuffer s = new StringBuffer(length*2);
       int end = offset+length;

       for (int i = offset; i < end; i++)
       {
         int high_nibble = (data[i] & 0xf0) >>> 4;
         int low_nibble = (data[i] & 0x0f);
         s.append(hex_table[high_nibble]);
         s.append(hex_table[low_nibble]);
       }

       return s.toString();
     }
