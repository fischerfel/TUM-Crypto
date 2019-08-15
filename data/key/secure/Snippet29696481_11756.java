            IvParameterSpec _IVParamSpec = new IvParameterSpec(IV);
        SecretKeySpec sks = new SecretKeySpec(KEY, "AES");
        cipher.init(Cipher.ENCRYPT_MODE, sks,_IVParamSpec);
        /*byte[] bt = new byte[256] ;
               bt = hexStringToByteArray(strToDecrypt);*/
        //final String decryptedString1 = new String(cipher.doFinal(bt));
                //final String decryptedString = new String(cipher.doFinal(strToDecrypt.getBytes("UTF-16LE")));
                final String decryptedString2 = new String(cipher.doFinal(Base64.decode(strToDecrypt, Base64.NO_WRAP)));
                String StrHex = toHex(decryptedString);
        final String decryptedString1 = new String(cipher.doFinal(strToDecrypt.getBytes()));
