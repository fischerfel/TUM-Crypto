public static byte[] decryptAES(byte[] key, byte[] text) throws Exception {   
            SecretKeySpec skeySpec = new SecretKeySpec(key, "AES");   
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS1Padding");   
            cipher.init(Cipher.DECRYPT_MODE, skeySpec);   
            byte[] decrypted = cipher.doFinal(text);   
            return decrypted;   
        }
