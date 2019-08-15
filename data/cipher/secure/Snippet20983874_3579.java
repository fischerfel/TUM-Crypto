            String key1 = "1234567812345678";
        byte[] key2 = key1.getBytes();


        SecretKeySpec secret = new SecretKeySpec(key2, "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");

        cipher.init(Cipher.ENCRYPT_MODE, secret);

        byte[] encrypted = cipher.doFinal(msg.getBytes(StandardCharsets.UTF_8));
        byte[] iv = cipher.getIV();

        String text = DatatypeConverter.printBase64Binary(encrypted);

        System.out.println("Encrypted info: " + text);

        bytebuf = ByteBuffer.allocate(1024);
        bytebuf.clear();

        // send iv

        bytebuf.put(iv);
        bytebuf.flip();
        while(bytebuf.hasRemaining()) {
            nBytes += client.write(bytebuf);
            System.out.println("Iv sent!");
        }

        bytebuf.clear();
        bytebuf.put(text.getBytes());

        bytebuf.flip();

        while(bytebuf.hasRemaining()) {
            nBytes += client.write(bytebuf);
        }
