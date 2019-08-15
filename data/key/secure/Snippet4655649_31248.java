public String encrypt(String data) throws Exception{
    try {
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        Key k = new SecretKeySpec(key.getBytes(), 0, key.length(), "AES");

        // Calculate ciphertext size.
        int blocksize = 16;
        int ciphertextLength = 0;
        int remainder = data.getBytes().length % blocksize;
        if (remainder == 0) {
            ciphertextLength = data.getBytes().length + blocksize;
        } else {
            ciphertextLength = data.getBytes().length - remainder + blocksize;
        }


        cipher.init(Cipher.ENCRYPT_MODE, k);
        byte[] buf = new byte[ciphertextLength];
        cipher.doFinal(data.getBytes(), 0, data.length(), buf, 0);

        StringBuffer strbuf = new StringBuffer(buf.length * 2);
        int i;

        for (i = 0; i < buf.length; i++) {
            if (((int) buf[i] & 0xff) < 0x10) {
                strbuf.append("0");
            }

            strbuf.append(Long.toString((int) buf[i] & 0xff, 16));
        }
        return strbuf.toString();
    } catch (Exception e) {
        Logger.logException(e);
    }
    return null;
}
