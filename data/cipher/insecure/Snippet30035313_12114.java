    try {
        SecretKeySpec secretKeySpec = new SecretKeySpec(byteKey, "DES");

        Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding"); //Request the use of the DES algorithm, using the ECB mode (Electronic CodeBook) and style padding PKCS-5.
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
        byte[] myCipherText = cipher.doFinal(plainText);
        byte[] test = (new String(myCipherText, "UTF8")).getBytes();

         System.out.println("\nStart decryption");
         cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
         byte[] newPlainText = cipher.doFinal(test);
         System.out.println(new String(newPlainText, "UTF8"));
    } catch (Exception e) {
        e.printStackTrace();
    }
