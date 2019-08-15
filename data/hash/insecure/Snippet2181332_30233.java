public class MD5ServletOutputStream extends ServletOutputStream {

    private final ServletOutputStream output;
    private final MessageDigest md5;

    {
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    public MD5ServletOutputStream(ServletOutputStream output) {
        this.output = output;
    }

    public void write(int i) throws IOException {
        byte[] b = { (byte) i };
        md5.update(b);
        output.write(b, 0, 1);
    }

    public byte[] getHash() {
        return md5.digest();
    }

}
