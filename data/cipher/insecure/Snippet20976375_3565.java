    String key1 = "1234567812345678";
    byte[] key2 = key1.getBytes();
    SecretKeySpec secret = new SecretKeySpec(key2, "AES");
    Cipher cipher = Cipher.getInstance("AES");
    cipher.init(Cipher.ENCRYPT_MODE, secret);
    byte[] encrypted = cipher.doFinal(msg.getBytes());
    System.out.println("Encrypted info: " + encrypted);

    String send = encrypted.toString();
    bytebuf = ByteBuffer.allocate(48);
    bytebuf.clear();
    bytebuf.put(send.getBytes());

    bytebuf.flip();

    while(bytebuf.hasRemaining()) {
        nBytes += client.write(bytebuf);
    }
