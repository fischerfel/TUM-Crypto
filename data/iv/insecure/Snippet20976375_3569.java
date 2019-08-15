            String key1 = "1234567812345678";
        byte[] key2 = key1.getBytes();
            byte[] iv = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
        IvParameterSpec ivspec = new IvParameterSpec(iv);
        SecretKeySpec secret = new SecretKeySpec(key2, "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, secret, ivspec);

        byte[] encrypted = cipher.doFinal(msg.getBytes(StandardCharsets.UTF_8));
        String text = DatatypeConverter.printBase64Binary(encrypted);

        System.out.println("Encrypted info: " + text);

        bytebuf = ByteBuffer.allocate(32);
        bytebuf.clear();

        bytebuf.put(text.getBytes());

        bytebuf.flip();

        while(bytebuf.hasRemaining()) {
            nBytes += client.write(bytebuf);
        }
