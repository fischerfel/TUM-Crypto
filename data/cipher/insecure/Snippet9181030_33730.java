public static byte [] ecbAesEncrypt(byte [] key, byte [] currentVector) {
        SecretKeySpec keySpec = null;
        Cipher cipher = null;
        byte [] encryptedValue = null;

        try {
            keySpec = new SecretKeySpec(key, "AES");

            cipher = Cipher.getInstance("AES/ECB/NoPadding");
            cipher.init(Cipher.ENCRYPT_MODE, keySpec);

            encryptedValue = cipher.doFinal(currentVector);
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        if (DEBUG)
            printHex("AES-ECB encrypt: ", encryptedValue);

        return encryptedValue;
    }
