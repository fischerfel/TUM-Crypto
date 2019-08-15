public class SecureSocket extends Socket {

    private SecretKey seckey;

    private InputStream in = null;
    private OutputStream out = null;
    private CipherInputStream cin = null;
    private CipherOutputStream cout = null;

    public SecureSocket() throws IOException {
    }

    public SecureSocket(String address, int port) throws UnknownHostException,
            IOException, NoSuchAlgorithmException, NoSuchPaddingException,
            InvalidKeyException {

        super(address, port);

        if (out == null) {
            this.out = super.getOutputStream();
        }

        if (in == null) {
            this.in = super.getInputStream();
        }

        KeyGenerator keyGen = KeyGenerator.getInstance("AES");
        SecureRandom random = new SecureRandom();
        keyGen.init(random);
        seckey = keyGen.generateKey();

        byte[] mess = EncryptionManager.rsaEncryptPublic(seckey.getEncoded());

        // writing the initial message with the AES encryption key
        out.write(mess);

        // Initialization of the Cipher streams
        Cipher cipherEn = Cipher.getInstance("AES");
        cipherEn.init(Cipher.ENCRYPT_MODE, seckey);
        Cipher cipherDc = Cipher.getInstance("AES");
        cipherDc.init(Cipher.DECRYPT_MODE, seckey);

        cout = new CipherOutputStream(out, cipherEn);
        cin = new CipherInputStream(in, cipherDc);

    }

    public InputStream getInputStream() throws IOException {
        if (cin == null)
            return super.getInputStream();
        return cin;
    }

    public OutputStream getOutputStream() throws IOException {
        if (cout == null)
            return super.getOutputStream();
        return cout;
    }

    public synchronized void close() throws IOException {
        OutputStream o = getOutputStream();
        o.flush();
        super.close();
    }

    public void setkey(SecretKey seckey) throws NoSuchAlgorithmException,
            NoSuchPaddingException, InvalidKeyException, IOException {

        this.seckey = seckey;

        Cipher cipherEn = Cipher.getInstance("AES");
        cipherEn.init(Cipher.ENCRYPT_MODE, seckey);

        cout = new CipherOutputStream(super.getOutputStream(), cipherEn);

        Cipher cipherDc = Cipher.getInstance("AES");
        cipherDc.init(Cipher.DECRYPT_MODE, seckey);

        cin = new CipherInputStream(super.getInputStream(), cipherDc);
    }

}
