public byte[] encrypt(byte[] origin)
    {
        String key = "testkey";
        SecretKeySpec sks = new SecretKeySpec(convertAESKey(key), "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        byte[] iv = new byte[16];
        cipher.init(Cipher.ENCRYPT_MODE, sks, new IvParameterSpec(iv));
        return cipher.doFinal(origin);
    }

private byte[] convertAESKey(String key)
   {
        byte[] keyBytes;
        keyBytes = key.getBytes("UTF-8");
        byte[] keyBytes16 = new byte[16];
        System.arraycopy(keyBytes, 0, keyBytes16, 0,
                Math.min(keyBytes.length, 16));
        return keyBytes16;
    }
}
