            LOGGER.info("Confirming write");

        byte[] iv = buf.array();

        LOGGER.info("Data packet found as {}", iv);


        LOGGER.info("Confirming write");
        String data = new String(buf.array());

        LOGGER.info("Data packet found as {}", data);


        IvParameterSpec ivspec = new IvParameterSpec(iv);
        String key1 = "1234567812345678";
        byte[] key2 = key1.getBytes();
        SecretKeySpec secret = new SecretKeySpec(key2, "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");


        cipher.init(Cipher.DECRYPT_MODE, secret, ivspec);

        byte[] encrypted = DatatypeConverter.parseBase64Binary(data);
        byte[] decrypted = cipher.doFinal(encrypted);

        System.out.println("Decrypted Info: " + new String(decrypted, StandardCharsets.UTF_8));
