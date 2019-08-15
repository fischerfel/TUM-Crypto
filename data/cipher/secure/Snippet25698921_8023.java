public void saveExportToFile(String fileName, BigInteger mod, BigInteger exp, String info, PublicKey puk) throws IOException {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    ObjectOutputStream oout = new ObjectOutputStream(new BufferedOutputStream(baos));
    try {
        oout.writeObject(mod);
        oout.writeObject(exp);
        oout.writeChars(info);
        oout.close();
        baos.close();
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, puk);

        FileOutputStream fos = new FileOutputStream(new File(fileName));
        BufferedOutputStream bos = new BufferedOutputStream(fos);

        byte[] data = baos.toByteArray();

        int i = 0;
        byte[] buffer = new byte[128];
        byte[] cipherData = null;
        while (i < data.length) {
            if (i+128 >= data.length) {
                buffer = new byte[data.length - i];
                System.arraycopy(data, i, buffer, 0, data.length - i);
                cipherData = cipher.doFinal(buffer);
                bos.write(cipherData);
            } else {
                System.arraycopy(data, i, buffer, 0, 128);
                cipherData = cipher.doFinal(buffer);
                bos.write(cipherData);
            }
            i += 128;
        }

        bos.close();
    } catch (Exception e) {
        throw new IOException("Unexpected error", e);
    }
}
