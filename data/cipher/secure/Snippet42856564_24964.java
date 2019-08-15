private static String encryptNew(String key, String initVector, String dataToEncrypt) throws Exception{

        byte[] plainTextbytes = dataToEncrypt.getBytes("UTF-8");
        byte[] keyBytes = key.getBytes("UTF-8");
        byte[] IvkeyBytes = initVector.getBytes("UTF-8");

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
        SecretKeySpec secretKeySpec = new SecretKeySpec(keyBytes, "AES");
        IvParameterSpec ivParameterSpec = new IvParameterSpec(IvkeyBytes);
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec);
        plainTextbytes = cipher.doFinal(plainTextbytes);
        return Base64.encodeToString(plainTextbytes, Base64.DEFAULT);
    }

    private static String decrypt(String key, String initVector, String dataToDecrypt) {
            try {

                byte[] cipheredBytes = Base64.decode(dataToDecrypt, Base64.DEFAULT);
                byte[] keyBytes = key.getBytes("UTF-8");
                byte[] IvkeyBytes = initVector.getBytes("UTF-8");

                Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
                SecretKeySpec secretKeySpecy = new SecretKeySpec(keyBytes, "AES");
                IvParameterSpec ivParameterSpec = new IvParameterSpec(IvkeyBytes);
                cipher.init(Cipher.DECRYPT_MODE, secretKeySpecy, ivParameterSpec);
                cipheredBytes = cipher.doFinal(cipheredBytes);

                return new String(cipheredBytes,"UTF-8");
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            return null;
        }
