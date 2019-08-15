    public class Encryption
    {
        private SecretKeyFactory factory;
        private SecretKey tmp;
        private SecretKey secret;
        private Cipher cipher;

        private byte[] iv;
        private byte[] cipherText;

        private final KeySpec spec = new PBEKeySpec("somepassword".toCharArray(), SALT, 65536, 256);
        private static final byte[] SALT = {(byte)0xc3, (byte)0x23, (byte)0x71, (byte)0x1c, (byte)0x2e, (byte)0xc2, (byte)0xee, (byte)0x77};

        public Encryption()
        {
            try
            {
                factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
                tmp = factory.generateSecret(spec);
                secret = new SecretKeySpec(tmp.getEncoded(), "AES");
                cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
                cipher.init(Cipher.ENCRYPT_MODE, secret);
                iv = cipher.getParameters().getParameterSpec(IvParameterSpec.class).getIV();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }

        public String encrypt(String valueToEncrypt) throws Exception
        {
            cipher.init(Cipher.ENCRYPT_MODE, secret, new IvParameterSpec(iv));
            cipherText = cipher.doFinal(Base64.decodeBase64(valueToEncrypt.getBytes()));
            return Base64.encodeBase64String(cipherText);
        }

        public String decrypt(String encryptedValueToDecrypt) throws Exception
        {
            cipher.init(Cipher.DECRYPT_MODE, secret, new IvParameterSpec(iv));
            return new String(cipher.doFinal(new Base64().encode(encryptedValueToDecrypt.getBytes())));
        }

        public static void main(String[] args ) throws Exception
        {
            Encryption manager = new Encryption();
            String encrypted = manager.encrypt("this is a string which i would like to encrypt");
            System.out.println(encrypted);
            String decrypted = manager.decrypt(encrypted);
            System.out.println(decrypted);
            System.out.println(encrypted.equals(decrypted));
        }
}
