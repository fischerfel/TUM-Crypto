public class ExecuteEncryptDecryptSample {
        private static String method="SHA1"; 
        public static SecretKeySpec getKeySpec() throws IOException, NoSuchAlgorithmException {
            byte[] bytes = new byte[16];
            File f = new File("sample_aes_key");
        SecretKey key = null;
        SecretKeySpec spec = null;
        if (f.exists()) {
            new FileInputStream(f).read(bytes);
        } else {
            //KeyGenerator kgen = KeyGenerator.getInstance("SHA1");//PBKDF2WithHmacSHA1
            KeyGenerator kgen = KeyGenerator.getInstance(method);
            kgen.init(256);
            key = kgen.generateKey();
            bytes = key.getEncoded();
            new FileOutputStream(f).write(bytes);

        }
        spec = new SecretKeySpec(bytes,method);
        return spec;
    }
    public static void encrypt(String text) throws Exception {
        SecretKeySpec spec = getKeySpec();
        Cipher cipher = Cipher.getInstance(method);
        cipher.init(Cipher.ENCRYPT_MODE, spec);
        BASE64Encoder enc = new BASE64Encoder();
        System.out.println(enc.encode(cipher.doFinal(text.getBytes())));
    }

    public static void main(String[] args) throws Exception {
        String text = "1234000156237828282873773";
        //Security security;
        //security.getProviders();
        System.out.println();
        encrypt(text);
    }
}
