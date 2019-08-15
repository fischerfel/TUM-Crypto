    Cipher encryptor = Cipher.getInstance("AES/CBC/PKCS5Padding");
    Cipher decryptor = Cipher.getInstance("AES/CBC/PKCS5Padding");

    encryptor.init(Cipher.ENCRYPT_MODE, secret);
    byte[] IV = encryptor.getIV();

    packet.setData(IV);
    socket.send(packet);

    IvParameterSpec ips = new IvParameterSpec(IV);

    decryptor.init(Cipher.DECRYPT_MODE, secret, ips);
