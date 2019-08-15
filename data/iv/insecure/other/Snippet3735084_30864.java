  public static byte[] decryptAES(byte[] key, byte[] text) throws Exception {   

          SecretKeySpec skeySpec = new SecretKeySpec(GetKey(key), "AES");

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding","BC");  

            byte [] iv = new byte[cipher.getBlockSize()];
            for(int i=0;i<iv.length;i++)iv[i] = 0;
            IvParameterSpec ivSpec = new IvParameterSpec(iv);
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, ivSpec);

            byte[] decrypted = cipher.doFinal(text);   
            return decrypted;   
        }
