byte[] buffer = new byte[8092];
try {       
    InputStream in = s.getInputStream();

    SecretKey key = new SecretKeySpec("1212121212121212".getBytes("UTF-8"),"AES");
    Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
    cipher.init(Cipher.DECRYPT_MODE, key, iv);

    while ((leido = in.read(buffer)) > 0) {
        cipher.update(buffer, 0, leido);
    }

    salida = decodificar(cipher.doFinal());
