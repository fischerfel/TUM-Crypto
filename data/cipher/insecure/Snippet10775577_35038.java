private static final String ALGORITHM = "Blowfish/CBC/PKCS5Padding";

/* now returns the IV that was used */
private static byte[] encrypt(SecretKey key, 
                              InputStream is, 
                              OutputStream os) {
    try {
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, key);
        CipherInputStream cis = new CipherInputStream(is, cipher);
        doCopy(cis, os);
        return cipher.getIV();
    } catch (Exception ex) {
        throw new RuntimeException(ex);
    }
}

private static void decrypt(SecretKey key, 
                            byte[] iv, 
                            InputStream is, 
                            OutputStream os) 
{
    try {
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        IvParameterSpec ivSpec = new IvParameterSpec(iv);
        cipher.init(Cipher.DECRYPT_MODE, key, ivSpec);
        CipherInputStream cis = new CipherInputStream(is, cipher);
        doCopy(cis, os);
    } catch (Exception ex) {
        throw new RuntimeException(ex);
    }
}

private static void doCopy(InputStream is, OutputStream os) 
throws IOException {
    try {
        byte[] bytes = new byte[4096];
        int numBytes;
        while ((numBytes = is.read(bytes)) != -1) {
            os.write(bytes, 0, numBytes);
        }
    } finally {
        is.close();
        os.close();
    }
}

public static void main(String[] args) {
    try {
        String plain = "I am very secret. Help!";

        KeyGenerator keyGen = KeyGenerator.getInstance("Blowfish");
        SecretKey key = keyGen.generateKey();
        byte[] iv;

        InputStream in = new ByteArrayInputStream(plain.getBytes("UTF-8"));
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        iv = encrypt(key, in, out);

        in = new ByteArrayInputStream(out.toByteArray());
        out = new ByteArrayOutputStream();
        decrypt(key, iv, in, out);

        String result = new String(out.toByteArray(), "UTF-8");
        System.out.println(result);
        System.out.println(plain.equals(result)); // => true
    } catch (Exception e) {
        e.printStackTrace();
    }
}
