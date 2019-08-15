        final String key = "=abcd!#Axd*G!pxP";
        final javax.crypto.spec.SecretKeySpec keySpec = new SecretKeySpec(key.getBytes(), "AES");
        final javax.crypto.Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, keySpec);
        byte [] encryptedValue = cipher.doFinal(input.getBytes());
        return new String(org.apache.commons.codec.binary.Hex.encodeHex(encryptedValue));
