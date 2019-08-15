    byte j[] = key.getBytes();
    SecretKeySpec kye = new SecretKeySpec(j, "AES");
    Cipher enc = Cipher.getInstance("AES");
    enc.init(Cipher.ENCRYPT_MODE, kye);
    FileOutputStream output = new FileOutputStream("xyz.mkv");
    CipherOutputStream cos = new CipherOutputStream(output, enc);
    byte[] buf = new byte[104857600];
    int read;
    while ((read = file.read(buf)) != -1) {
        cos.write(buf, 0, read);
    }
    output.flush();
    buf = null;
    file.close();
    cos.close();
