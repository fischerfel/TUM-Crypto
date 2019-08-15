        byte[] keyPass = pass.getBytes("ASCII");
        final Key key = new SecretKeySpec(keyPass, "AES");
        final Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
        byte[] byteMessage = text.getBytes("UTF-8");
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] cipherText = cipher.doFinal(byteMessage);
        byte[] ivByte = cipher.getIV();

        byte[] bytesTotal = new byte[ivByte.length+cipherText.length];
        System.arraycopy(ivByte, 0, bytesTotal, 0, ivByte.length);
        System.arraycopy(cipherText, 0, bytesTotal, ivByte.length, cipherText.length);
        encyoted = Base64.encodeToString(bytesTotal, Base64.NO_WRAP);
