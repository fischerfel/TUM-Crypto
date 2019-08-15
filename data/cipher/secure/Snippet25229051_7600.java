public class AES_GUI {
    private String algo;
    private String path;
    private String password;

    public AES_GUI(String algo, String path, String password) {
        this.algo = algo; // setting algo
        this.path = path;// setting file path
        this.password = password;
    }

    public void encrypt() throws Exception {
        SecureRandom padding = new SecureRandom();
        byte[] salt = new byte[16];
        padding.nextBytes(salt);
        // generating key
        byte k[] = password.getBytes("UTF-8");
        MessageDigest sha = MessageDigest.getInstance("SHA-1");
        sha.update(salt);
        k = sha.digest(k);
        k = Arrays.copyOf(k, 16);
        for (int i = 0; i < k.length; i++)
            System.out.print(k[i]);
        System.out.println();

        SecretKeySpec key = new SecretKeySpec(k, algo);
        // creating and initialising cipher and cipher streams
        Cipher encrypt = Cipher.getInstance(algo);
        encrypt.init(Cipher.ENCRYPT_MODE, key);
        // opening streams
        FileOutputStream fos = new FileOutputStream(path + ".enc");
        fos.write(salt);
        try (FileInputStream fis = new FileInputStream(path)) {
            try (CipherOutputStream cout = new CipherOutputStream(fos, encrypt)) {
                copy(fis, cout);
            }
        }
    }

    public void decrypt() throws Exception {
        BufferedInputStream fis = new BufferedInputStream(new FileInputStream(path + ".enc"), 16);

        byte[] salt = new byte[16];
        int read = fis.read(salt);
        if (read != 16) {
            throw new IllegalStateException();
        }

        // generating same key
        byte k[] = password.getBytes("UTF-8");
        MessageDigest sha = MessageDigest.getInstance("SHA-1");
        sha.update(salt);
        k = sha.digest(k);
        k = Arrays.copyOf(k, 16);
        for (int i = 0; i < k.length; i++)
            System.out.print(k[i]);
        System.out.println();
        SecretKeySpec key = new SecretKeySpec(k, algo);
        // creating and initialising cipher and cipher streams
        Cipher decrypt = Cipher.getInstance(algo);
        decrypt.init(Cipher.DECRYPT_MODE, key);
        // opening streams
        try (CipherInputStream cin = new CipherInputStream(fis, decrypt)) {
            try (FileOutputStream fos = new FileOutputStream(path + ".dec")) {
                copy(cin, fos);
            }
        }
    }

    private void copy(InputStream is, OutputStream os) throws Exception {
        byte buf[] = new byte[4096]; // 4K buffer set
        int read = 0;
        while ((read = is.read(buf)) != -1)
            // reading
            os.write(buf, 0, read); // writing
    }
}
