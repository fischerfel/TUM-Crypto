    if (encrypted != null)
    {
        //Get the passphras, salt, IV and msg
        String data[] = encrypted.split(":");
        String passphrase = data[0];
        String salt_hex = data[1];
        String iv_hex = data[2];
        String msg64 = data[3];
        String jskey_hex = data[4];
        byte[] jskey = hexStringToByteArray(jskey_hex);
        byte[] iv = hexStringToByteArray(iv_hex);
        byte[] salt = hexStringToByteArray(salt_hex);
        BASE64Decoder decoder = new BASE64Decoder();
        byte[] msg = decoder.decodeBuffer(msg64);

        try {
             //theClear = AES.decrypt(encrypted);
            /* Decrypt the message, given derived key and initialization vector. */
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            KeySpec spec = new PBEKeySpec(passphrase.toCharArray(), salt, 10, 256/32);
            SecretKey key = new SecretKeySpec(factory.generateSecret(spec).getEncoded(), "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(iv));
            String plaintext = new String(cipher.doFinal(msg), "UTF-8");
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
