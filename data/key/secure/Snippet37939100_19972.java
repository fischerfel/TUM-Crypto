public byte[] encrypt(byte[] toEncrypt) throws Exception {
    try {
        SecretKeySpec secretKeySpec = new SecretKeySpec(getRawKey(Utils.generateUID().getBytes()), "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
        byte[] iv = cipher.getIV();
        byte[] ct = cipher.doFinal(toEncrypt);

        byte[] result = new byte[ct.length + iv.length];
        System.arraycopy(iv, 0, result, 0, iv.length);
        System.arraycopy(ct, 0, result, iv.length, ct.length);
        return result;
    } catch(...) {...}
    return new byte[0];
}

public byte[] decrypt(byte[] encryptedByte) throws Exception {
    try {
        SecretKeySpec secretKeySpec = new SecretKeySpec(getRawKey(Utils.generateUID().getBytes()), "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");

        byte[] iv = new byte[cipher.getBlockSize()];
        byte[] ct = new byte[encryptedByte.length - cipher.getBlockSize()];
        System.arraycopy(encryptedByte, 0, iv, 0, cipher.getBlockSize());
        System.arraycopy(encryptedByte, cipher.getBlockSize(), ct, 0, ct.length);

        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, new IvParameterSpec(iv));
        return cipher.doFinal(ct);
    } catch (...) {...}
    return new byte[0];
}
