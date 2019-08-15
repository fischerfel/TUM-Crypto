    public static void encrypt() throws Exception {
    final byte[] buf = new byte[8192];
    final Cipher c = Cipher.getInstance("AES/CTR/NoPadding");
    c.init(Cipher.ENCRYPT_MODE, new SecretKeySpec("1234567890123456".getBytes(), "AES"), new IvParameterSpec(new byte[16]));
    final InputStream is = new FileInputStream(Environment.getExternalStorageDirectory().getAbsolutePath() + "/AB/"+"a.mp4");
    final OutputStream os = new CipherOutputStream(new FileOutputStream(Environment.getExternalStorageDirectory().getAbsolutePath() + "/AB/"+"b.mp4"), c);
    while (true) {
        int n = is.read(buf);
        if (n == -1) break;
        os.write(buf, 0, n);
    }
    os.close(); is.close();
}
