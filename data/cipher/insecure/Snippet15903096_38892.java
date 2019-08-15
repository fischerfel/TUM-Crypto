public class Test {
        private static File file;
        private static final byte[] STAT_KEY = { -1, -2, 3, 4, -5, -6, -7, 8 };
        static {
            file = new File("MyFile.txt");
        }

        private static Cipher getCipher(int mode) throws InvalidKeyException, NoSuchAlgorithmException,
                InvalidKeySpecException, NoSuchPaddingException {
            DESKeySpec dks = new DESKeySpec(STAT_KEY);
            SecretKeyFactory skf = SecretKeyFactory.getInstance("DES");
            SecretKey desKey = skf.generateSecret(dks);
            Cipher cipher = Cipher.getInstance("DES");
            cipher.init(mode, desKey);
            return cipher;
        }

        private static void appendToFile(String item) throws Exception {
            CipherOutputStream cos = null;
            try {
                cos = new CipherOutputStream(new FileOutputStream(file, true), getCipher(Cipher.ENCRYPT_MODE));
                cos.write((item + String.format("%n")).getBytes());
            } finally {
                cos.close();
            }
        }

        private static void readFromFile() throws Exception {
            CipherInputStream cis = null;
            try {
                cis = new CipherInputStream(new FileInputStream(file), getCipher(Cipher.DECRYPT_MODE));
                int content;
                while ((content = cis.read()) != -1) {
                    System.out.print((char) content);
                }
            } finally {
                cis.close();
            }
        }

        public static void main(String[] args) throws Exception {
            String[] items = { "Hello", "dear", "world" };
            for (String item : items) {
                appendToFile(item);
            }
            readFromFile();
        }
    }
