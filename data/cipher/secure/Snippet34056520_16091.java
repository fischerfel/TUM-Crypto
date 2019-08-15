 public byte[] encrypt(byte[] key, byte[] input) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding ");
        cipher.init(Cipher.ENCRYPT_MODE, key);
        return cipher.doFinal(input);
    }
