public static void main(String[] args) throws KeyStoreException, NoSuchAlgorithmException, UnrecoverableKeyException, IllegalBlockSizeException, InvalidKeyException, BadPaddingException, NoSuchPaddingException, IOException, CertificateException {
        Provider provider = new sun.security.pkcs11.SunPKCS11(DesSaveLoad.class.getClassLoader().getResourceAsStream("pkcs11.cfg"));
        Security.removeProvider(provider.getName());
        Security.insertProviderAt(provider, 1);
        KeyStore keyStore = KeyStore.getInstance("PKCS11", provider);
        keyStore.load(null, null);
        SecretKey desKey = desGenerateKey();
        keyStore.setKeyEntry("t1", desKey, null, null);
        SecretKey t1 = (SecretKey) keyStore.getKey("t1", null);
        byte[] messageBytes = "message".getBytes();
        desEncrypt(messageBytes, 0, messageBytes.length, desKey);
        desEncrypt(messageBytes, 0, messageBytes.length, t1);  //Exception is thrown here
    }

    public static SecretKey desGenerateKey() throws NoSuchAlgorithmException {
        KeyGenerator keygenerator = null;
        keygenerator = KeyGenerator.getInstance("DES");
        return keygenerator.generateKey();
    }

    public static byte[] desEncrypt(byte[] plainText, int offset, int size, SecretKey key) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        Cipher cipher;
        if (size % 8 != 0) {
            cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
        } else {
            cipher = Cipher.getInstance("DES/ECB/NoPadding");
        }
        cipher.init(Cipher.ENCRYPT_MODE, key);
        return cipher.doFinal(plainText, offset, size);
    }
