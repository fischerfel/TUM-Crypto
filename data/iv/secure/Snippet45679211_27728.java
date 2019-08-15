private static final String SECRET_KEY_ALGO = "AES";
private static final int SECRET_KEY_SIZE_IN_BITS = 256;
private static final String AES_TRANSFORMATION = "AES/CBC/PKCS5Padding";
private static final int DEFAULT_BUFFERSIZE = 8 * 1024;

public static void main(String[] args) throws IOException {
    String expected = randomText();
    byte[] textBytes = expected.getBytes();
    EncryptedOutputStreamWrapper enc = new EncryptedOutputStreamWrapper();
    {
        InputStream in = new ByteArrayInputStream(textBytes);
        ZipOutputStream out = new ZipOutputStream(enc.wrap(new FileOutputStream("f.zip")));
        out.putNextEntry(new ZipEntry("_"));
        IOUtils.copy(in, out);
        in.close();
        out.closeEntry();
        out.close();
    }
    //
    DecryptedInputStreamWrapper dec = new DecryptedInputStreamWrapper(enc.getSKey(), enc.getIv());
    {
        ZipInputStream in = new ZipInputStream(dec.wrap(new FileInputStream("f.zip")));
        OutputStream out = new FileOutputStream("f.txt");
        in.getNextEntry();
        IOUtils.copy(in, out);
        in.closeEntry();
        in.close();
        out.close();
    }
    //
    String actual = new String(IOUtils.toByteArray(new FileInputStream("f.txt")));
    if (!expected.equals(actual)) {
        System.out.println("Fail!");
        System.out.println("Expected '" + expected + "'");
        System.out.println();
        System.out.println("Actual: '" + actual + "'");
    } else {
        System.out.println("Success!");
    }
}

public static class EncryptedOutputStreamWrapper {
    private Cipher cipher;
    private SecretKey sKey;
    private byte[] iv;

    public EncryptedOutputStreamWrapper() {
        try {
            KeyGenerator generator = KeyGenerator.getInstance(SECRET_KEY_ALGO);
            generator.init(SECRET_KEY_SIZE_IN_BITS);
            this.sKey = generator.generateKey();
            this.cipher = Cipher.getInstance(AES_TRANSFORMATION);
            this.cipher.init(Cipher.ENCRYPT_MODE, sKey);
            this.iv = cipher.getIV();
        } catch (Exception e) {
            throw new CipherException("Error encrypting", e);
        }
    }

    public OutputStream wrap(final OutputStream out) {
        return new BufferedOutputStream(new OutputStream() {
            @Override
            public void write(int b) throws IOException {
            }

            @Override
            public void write(byte[] plainBytes, int off, int len) throws IOException {
                byte[] encryptedBytes = cipher.update(plainBytes, off, len);
                if (encryptedBytes != null) {
                    out.write(encryptedBytes, 0, encryptedBytes.length);
                }
            }

            @Override
            public void flush() throws IOException {
                out.flush();
            }

            @Override
            public void close() throws IOException {
                try {
                    byte[] encryptedBytes = cipher.doFinal();
                    if (encryptedBytes != null) {
                        out.write(encryptedBytes, 0, encryptedBytes.length);
                    }
                } catch (Exception e) {
                    throw new IOException("Error encrypting", e);
                }
                out.close();
            }
        });
    }

    public SecretKey getSKey() {
        return sKey;
    }

    public byte[] getIv() {
        return iv;
    }

}

public static class DecryptedInputStreamWrapper {
    private Cipher cipher;

    public DecryptedInputStreamWrapper(SecretKey sKey, byte[] iv) {
        try {
            this.cipher = Cipher.getInstance(AES_TRANSFORMATION);
            this.cipher.init(Cipher.DECRYPT_MODE, sKey, new IvParameterSpec(iv));
        } catch (Exception e) {
            throw new CipherException("Error decrypting", e);
        }
    }

    public InputStream wrap(final InputStream in) {
        return new BufferedInputStream(new InputStream() {
            private byte[] buffer = new byte[DEFAULT_BUFFERSIZE];
            private boolean done;

            @Override
            public int read() throws IOException {
                return 0;
            }

            @Override
            public int read(byte[] bytes, int off, int len) throws IOException {
                if (done) {
                    return -1;
                }
                int encryptedLen = in.read(buffer);
                try {
                    byte[] plainBytes = null;
                    if (encryptedLen == -1) {
                        done = true;
                        plainBytes = cipher.doFinal();
                    } else {
                        plainBytes = cipher.update(buffer, 0, encryptedLen);
                    }
                    if (plainBytes != null) {
                        System.arraycopy(plainBytes, 0, bytes, off, plainBytes.length);
                        return plainBytes.length;
                    }
                } catch (Exception e) {
                    throw new IOException("Error decrypting", e);
                }
                return 0;
            }

            @Override
            public void close() throws IOException {
                in.close();
            }

        });
    }
}

public static class CipherException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public CipherException() {
        super();
    }

    public CipherException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public CipherException(String message, Throwable cause) {
        super(message, cause);
    }

    public CipherException(String message) {
        super(message);
    }

    public CipherException(Throwable cause) {
        super(cause);
    }

}

public static String randomText() {
    StringBuilder sb = new StringBuilder();
    int textLength = rnd(100000, 999999);
    for (int i = 0; i < textLength; i++) {
        if (rnd(0, 1) == 0) {
            sb.append((char) rnd(65, 90));
        } else {
            sb.append((char) rnd(49, 57));
        }
    }
    return sb.toString();
}

public static int rnd(int min, int max) {
    return min + (int) (Math.random() * ((max - min) + 1));
}
