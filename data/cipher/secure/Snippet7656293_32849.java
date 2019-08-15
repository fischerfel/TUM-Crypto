public void encrypt(File in, File out) {


    try {
        aesCipher.getInstance("AES/CBC/PKCS5Padding");
        aesCipher.init(Cipher.ENCRYPT_MODE, aeskeySpec);
        FileInputStream is;
        is = new FileInputStream(in);
        CipherOutputStream os = new CipherOutputStream(new FileOutputStream(out), aesCipher);
        copy(is, os);
        os.close();
    } catch (FileNotFoundException ex) {
        Logger.getLogger(FileEncryption.class.getName()).log(Level.SEVERE, null, ex);
    } catch (IOException ex) {
        Logger.getLogger(FileEncryption.class.getName()).log(Level.SEVERE, null, ex);
    } catch (InvalidKeyException ex) {
        Logger.getLogger(FileEncryption.class.getName()).log(Level.SEVERE, null, ex);
    } catch (NoSuchAlgorithmException ex) {
        Logger.getLogger(FileEncryption.class.getName()).log(Level.SEVERE, null, ex);
    } catch (NoSuchPaddingException ex) {
        Logger.getLogger(FileEncryption.class.getName()).log(Level.SEVERE, null, ex);
    }

}

private void copy(InputStream is, OutputStream os) throws IOException {
    int i;
    byte[] b = new byte[2048];
    while ((i = is.read(b)) != -1) {
        os.write(b, 0, i);
    }
}
