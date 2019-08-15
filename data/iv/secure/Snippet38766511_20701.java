            byte[] cipherText = encryptedText.getBytes("UTF-8");

            Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
            SecretKeySpec key = new SecretKeySpec(KEY.getBytes("UTF-8"), "AES");
            cipher.init(Cipher.DECRYPT_MODE, key,new IvParameterSpec(IV.getBytes("UTF-8")));
            return new String(cipher.doFinal(cipherText),"UTF-8");
