    cipher = Cipher.getInstance("AES");

    cipher.init(Cipher.ENCRYPT_MODE, skeySpec);

    encrypted = cipher.doFinal(str.getBytes())
