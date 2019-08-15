public byte[] encrypt(byte[] in) {
    byte[] encrypted = null;
    try {
        aesCipher.getInstance("AES/CBC/PKCS5Padding");
        aesCipher.init(Cipher.ENCRYPT_MODE, aeskeySpec);
        ByteArrayInputStream bais = new ByteArrayInputStream(in);
        ByteArrayOutputStream baos = new ByteArrayOutputStream(bais.available());
        CipherOutputStream os = new CipherOutputStream(baos, aesCipher);
        copy(bais, os);
        os.flush();
        byte[] raw = baos.toByteArray();
        os.close();
        encrypted = Base64.encodeBase64(raw);

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
    return encrypted;
}
