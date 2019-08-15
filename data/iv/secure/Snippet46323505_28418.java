public String encryptString(String plainText) {
        byte[] ciphertext;
        byte[] iv = new byte[16];
        byte[] plainBytes = plainText.getBytes(StandardCharsets.UTF_8);
        String _signKey = "****************************************************************";
        String _encKey = "****************************************************************";



        try {
            Mac sha256 = Mac.getInstance("HmacSHA256");
            SecretKeySpec shaKS = new SecretKeySpec(_signKey.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
            sha256.init(shaKS);

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            SecureRandom randomSecureRandom = SecureRandom.getInstance("SHA1PRNG");
            iv = new byte[cipher.getBlockSize()];
            randomSecureRandom.nextBytes(iv);
            IvParameterSpec ivParams = new IvParameterSpec(iv);
            byte[] sessionKey = sha256.doFinal((_encKey + iv).getBytes(StandardCharsets.UTF_8));
            // Perform Encryption
            SecretKeySpec eks = new SecretKeySpec(sessionKey, "AES");
            cipher.init(Cipher.ENCRYPT_MODE, eks, ivParams);

            ciphertext = cipher.doFinal(plainBytes);
            System.out.println("ciphertext= " + new String(ciphertext));
            // Perform HMAC using SHA-256 on ciphertext
            SecretKeySpec hks = new SecretKeySpec(_signKey.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(hks);

            ByteArrayOutputStream outputStream2 = new ByteArrayOutputStream();
            outputStream2.write(iv);
            outputStream2.write(ciphertext);
            outputStream2.flush();
            outputStream2.write(mac.doFinal(outputStream2.toByteArray()));
            return Base64.encodeBase64String(outputStream2.toByteArray());

        } catch (Exception e) {
            e.printStackTrace();
        }
        return plainText;
    }
