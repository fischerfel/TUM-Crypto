public static String aes128(String key, String data, final int direction) {
    // PADCHAR = (char)0x10 as String
    while (key.length() % 16 > 0)
        key = key + PADCHAR; // Added this loop

    SecureRandom rand = new SecureRandom(key.getBytes());
    byte[] randBytes = new byte[16];
    rand.nextBytes(randBytes);
    SecretKey encKey = new SecretKeySpec(randBytes, "AES");
    AlgorithmParameterSpec paramSpec = new IvParameterSpec(key.getBytes()); // Created this

    Cipher cipher = null;
    try {
        cipher = Cipher.getInstance("AES/CBC/NoPadding"); // Added CBC/NoPadding
        cipher.init((direction == ENCRYPT ? Cipher.ENCRYPT_MODE : Cipher.DECRYPT_MODE), encKey, paramSpec); // Added paramSpec
    } catch (InvalidKeyException e) {
        return null;
    } catch (NoSuchPaddingException e) {
        return null;
    } catch (NoSuchAlgorithmException e) {
        return null;
    } catch (InvalidAlgorithmParameterException e) {
        return null; // Added this catch{}
    }

    try {
        if (direction == ENCRYPT) {
            while (data.length() % 16 > 0)
                data = data + PADCHAR; // Added this loop

            byte[] encVal = cipher.doFinal(data.getBytes());
            String encryptedValue = Base64.encode(encVal);
            return encryptedValue;
        } else {
            byte[] dataBytes = Base64.decode(data);
            byte[] encVal = cipher.doFinal(dataBytes);
            return new String(encVal);
        }
    } catch (NullPointerException e) {
        return null;
    } catch (BadPaddingException e) {
        return null;
    } catch (IllegalBlockSizeException e) {
        return null;
    }
}
