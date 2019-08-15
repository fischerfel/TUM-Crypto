 try
       {
           byte[] IV = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 0, 1, 2, 3, 4, 5, 6 };
           byte[] KEY = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 0, 1, 2, 3, 4, 5, 6 };

           SecretKeySpec key = new SecretKeySpec(KEY, "AES/CBC/PKCS7Padding");
           Cipher cipher = Cipher.getInstance ("AES/CBC/PKCS7Padding");

           cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(IV));

           byte[] bytesData = Utilities.Base64Coder.decode(data);

           Log.i("bytesData", String.valueOf(bytesData.length));

           String strResult = new String(cipher.doFinal(bytesData));


            Log.i("decrypted string", strResult);
            return strResult;
        }
        catch (Exception e) {

            Log.i("decrypted FAILED", e.getMessage());
            return null;
        }
