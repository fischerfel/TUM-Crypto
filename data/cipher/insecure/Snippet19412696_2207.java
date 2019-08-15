    public static void encrypt(String key, String filename) throws Throwable {
        InputStream is = new FileInputStream("Somefile.class");
        OutputStream os = new FileOutputStream("tempfile.class");

        DESKeySpec dks = new DESKeySpec(key.getBytes());
        SecretKeyFactory skf = SecretKeyFactory.getInstance("DES");
        SecretKey desKey = skf.generateSecret(dks);
        Cipher cipher = Cipher.getInstance("DES"); // DES/ECB/PKCS5Padding for SunJCE

            cipher.init(Cipher.ENCRYPT_MODE, desKey);
            CipherInputStream cis = new CipherInputStream(is, cipher);
            doCopy(cis, os);

            File file2 = new File("tempfile.class");    

            File f = new File("somefile.class");
            f.delete();
            file2.renameTo(f);
    }
