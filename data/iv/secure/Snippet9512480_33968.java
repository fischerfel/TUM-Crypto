public void encrypt(InputStream in, OutputStream out) throws Exception {
    final SecretKey key = new SecretKeySpec(keyBytes, "DESede");
    final IvParameterSpec param = new IvParameterSpec(iv);
    final Cipher cipher = Cipher.getInstance("DESede/CBC/PKCS5Padding");
    cipher.init(Cipher.ENCRYPT_MODE, key, param);

    // Read in the cleartext bytes and write to out to encrypt
    int numRead = 0;
    while ((numRead = in.read(buf)) >= 0) {
        byte[] output = cipher.doFinal(buf, 0, numRead);
        if(output != null) {
            byte[] enc = Base64.encode(output, 0);
            out.write(enc);
        }   
    }
    out.close();
}
