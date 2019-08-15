String facebookId = "123456789";
        String keyCrypt = "1231238912389123asdasdklasdkjasd";

        try {
            SecretKeySpec skeySpec = new SecretKeySpec(keyCrypt.getBytes(),
                    "AES");
            Cipher enCipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            byte[] ivData = new byte[enCipher.getBlockSize()];
            IvParameterSpec iv = new IvParameterSpec(ivData);
            enCipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);

            byte[] encryptedBytes = enCipher.doFinal(facebookId.getBytes());

            String ivEncrypted = new String(ivData)
                    + new String(encryptedBytes);

            String strEncode = Base64
                    .encodeBase64String(ivEncrypted.getBytes());

            System.out.println(strEncode);

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
