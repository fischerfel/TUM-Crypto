public String encrypt(String dataToEncrypt)
            throws NoSuchAlgorithmException, NoSuchPaddingException,
            InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        // I'm using AES encription

        if (!dataToEncrypt.equals("")) {
            String key = "rEqrHrhdd9I1sg==";

            Cipher c = Cipher.getInstance("AES");
            SecretKeySpec k;
            try {
                k = new SecretKeySpec(key.getBytes(), "AES");
                c.init(Cipher.ENCRYPT_MODE, k);
            } catch (Exception e) {
                e.printStackTrace();
            }

            return new String(c.doFinal(Base64.decode(dataToEncrypt, 0)));
        }
        return "";
    }
