public static String decryptAES(String cryptedString, byte[] byteArrayAESKey) {

    try {
        IvParameterSpec ips = new IvParameterSpec(General.InitVector.getBytes("UTF-8"));
        SecretKey aesKey = new SecretKeySpec(byteArrayAESKey, "AES");
        byte[] TBCrypt = Base64.decode(cryptedString, Base64.DEFAULT);
        // Decryption cipher
        Cipher decryptCipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
        // Initialize PBE Cipher with key and parameters
        decryptCipher.init(Cipher.DECRYPT_MODE, aesKey, ips);
        // Decrypt the cleartext
        byte[] deciphertext = decryptCipher.doFinal(TBCrypt); // this may take a long time depending on string input length
        return new String(deciphertext, "UTF-8");
    } catch (Exception e) {
        e.printStackTrace();
        Log.e("AES", "Decrypt failed : " + e.getMessage());
        return "";
    }
    }
