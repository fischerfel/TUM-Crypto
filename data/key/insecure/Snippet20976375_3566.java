    // Server receives data and decrypts

    SocketChannel socket = (SocketChannel) key.channel();
    ByteBuffer buf = ByteBuffer.allocate(1024);
    nBytes = socket.read(buf);
    String data = new String(buf.array()).trim();
    String key1 = "1234567812345678";
    byte[] key2 = key1.getBytes();
    SecretKeySpec secret = new SecretKeySpec(key2, "AES");

    Cipher cipher = Cipher.getInstance("AES");

    cipher.init(Cipher.DECRYPT_MODE, secret);
    byte[] decrypted = cipher.doFinal(data.getBytes());
    System.out.println("Decrypted Info: " + new String(decrypted));
