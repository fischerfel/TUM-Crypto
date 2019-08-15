class AESEncryption {

    byte[] keyBytes;
    Cipher cipher;
    SecretKeySpec key;

    public AESEncryption() {

        try {
            cipher = Cipher.getInstance("AES/ECB/NoPadding", "BC");
        } catch (NoSuchAlgorithmException | NoSuchProviderException | NoSuchPaddingException e) {
            System.out.println(e.getMessage());
        }
    }

    public void createKey(byte[] keyBytes) {
        this.keyBytes = keyBytes;
        key = new SecretKeySpec(keyBytes, "AES");
    }

    public byte[] encrypt(byte[] plainText) {
        try {
            cipher.init(Cipher.ENCRYPT_MODE, key);
            return cipher.doFinal(plainText);
        } catch (InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }
}
