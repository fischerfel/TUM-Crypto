public class SecureServerSocket extends ServerSocket {

    public SecureServerSocket(int port) throws IOException {
        super(port);
    }

    public Socket accept() throws IOException {
        SecureSocket s = new SecureSocket();
        implAccept(s);

        SecretKey seckey;
        InputStream is = s.getInputStream();

        byte[] tmp = new byte[128]; //128: length of the key
        int i = 0;
        while (i < 128) {
            tmp[i] = (byte) (is.read() & 0x000000FF);
            ++i;
        }

        byte[] mess = EncryptionManager.rsaDecryptPrivate(tmp);

        seckey = new SecretKeySpec(mess, "AES");

        try {
            s.setkey(seckey);
        } catch (InvalidKeyException | NoSuchAlgorithmException
                | NoSuchPaddingException e) {
            e.printStackTrace();
        }
        return s;
    }
}
