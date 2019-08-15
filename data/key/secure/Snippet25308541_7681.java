public byte[] encryptBytes(byte[] source) {
        try {
            byte[] raw = key.getBytes(Charset.forName("UTF-8"));
            SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec);

            byte[] encrypted = cipher.doFinal(source);

            return encrypted;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
