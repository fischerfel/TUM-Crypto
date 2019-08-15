//Generate key
        byte[] key = new byte[8];
        for (int i = 0; i < 8; i++) {
            if (password.length() > i) {
                key[i] = password.getBytes()[i];
            } else {
                key[i] = (byte) i;
            }
        }
        //Setup cipher streams
        SecretKey key64 = new SecretKeySpec(key, "Blowfish");
        Cipher cipheren = Cipher.getInstance("Blowfish");
        cipheren.init(Cipher.ENCRYPT_MODE, key64);
        Cipher cipherde = Cipher.getInstance("Blowfish");
        cipheren.init(Cipher.DECRYPT_MODE, key64);
        out = new ObjectOutputStream(new CipherOutputStream(socket.getOutputStream(), cipheren));
        out.reset();
        out.flush();
        out.writeObject("switch");
        in = new ObjectInputStream(new CipherInputStream(socket.getInputStream(), cipherde));
