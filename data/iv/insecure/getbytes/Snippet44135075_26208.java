public class RijndaelCrypt {


     //private String key = "2a4e2471c77344b3bf1de28ab9aa492a444abc1379c3824e3162664a2c2b811d";
    private static String iv = "beadfacebadc0fee";
    private static String hashedKey = "6a2dad9f75b87f5bdd365c9de0b9c842";
    private static Cipher cipher;


    public static String decrypt(String text) throws UnsupportedEncodingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException, NoSuchProviderException {

     SecretKeySpec keyspec = new SecretKeySpec(hashedKey.getBytes("UTF-8"), "AES");
     IvParameterSpec ivspec = new IvParameterSpec(iv.getBytes("UTF-8"));
     Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
        cipher.init(Cipher.DECRYPT_MODE, keyspec, ivspec);

            byte[] decodedValue = Base64.decode(text.getBytes("UTF-8"));
            byte[] decryptedVal = cipher.doFinal(decodedValue);
            return new String(decryptedVal);              

    }

      public static String encryptNew(String data) throws Exception {

            cipher = Cipher.getInstance("AES/CBC/NoPadding");
            int blockSize = cipher.getBlockSize();

            byte[] dataBytes = data.getBytes("UTF-8");
            int plaintextLength = dataBytes.length;
            if (plaintextLength % blockSize != 0) {
                plaintextLength = plaintextLength + (blockSize - (plaintextLength % blockSize));
            }

            byte[] plaintext = new byte[plaintextLength];
            System.arraycopy(dataBytes, 0, plaintext, 0, dataBytes.length);

            SecretKeySpec keyspec = new SecretKeySpec(hashedKey.getBytes("UTF-8"), "AES");
            IvParameterSpec ivspec = new IvParameterSpec(iv.getBytes("UTF-8"));

            cipher.init(Cipher.ENCRYPT_MODE, keyspec, ivspec);
            byte[] encrypted = cipher.doFinal(plaintext);

            return DatatypeConverter.printBase64Binary(encrypted);

         }

    public static void main (String [] args) throws Exception
    {
        Security.addProvider(new BouncyCastleProvider());
        String data = "Hello";


        System.out.println("New Decrypted: " + RijndaelCrypt.decrypt(RijndaelCrypt.encryptNew(data)));
        System.out.println("New Encryption: " + RijndaelCrypt.encryptNew(data));
         }

   }
