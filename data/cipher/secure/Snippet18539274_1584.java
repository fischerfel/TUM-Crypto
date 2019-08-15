 public static void main(String[] args) throws Exception {
        Security.addProvider(new BouncyCastleProvider());
        KeyPair keyPair = readKeyPair(privateKey, "testpassword".toCharArray());
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, keyPair.getPublic());
        byte[] textEncrypted = cipher.doFinal("hello world".getBytes());
        System.out.println("encrypted: "+new String(textEncrypted));
        cipher.init(Cipher.DECRYPT_MODE, keyPair.getPrivate());
        byte[] textDecrypted = cipher.doFinal(textEncrypted);
        System.out.println("decrypted: "+new String(textDecrypted));
    }

    private static KeyPair readKeyPair(File privateKey, char[] keyPassword) throws IOException {
        FileReader fileReader = new FileReader(privateKey);
        PEMReader r = new PEMReader(fileReader, new DefaultPasswordFinder(keyPassword));
        try {
            return (KeyPair) r.readObject(); // this returns null
        } catch (IOException ex) {
            throw new IOException("The private key could not be decrypted", ex);
        } finally {
            r.close();
            fileReader.close();
        }
    }
