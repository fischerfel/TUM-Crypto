    public static void main(String[] args) throws Exception {

      // Server Side Generates KeyPair
      KeyPair keyPair = serverSideKeyGeneration();

      // Client receives the KeyPair or Public Key before sending actual call to server
      String originalString = "Hello";
      byte[] ecryptedBase64Data = clientSideCodeToGenerateEncryptedData(originalString, keyPair);
      System.out.println("Encrypted Data" + ecryptedBase64Data);

      // Server receives the encrypted Data and decrypt using Private Key
      String originalValue = decryptEncodedString(keyPair, ecryptedBase64Data);
      System.out.println(originalValue);

    }

    private static byte[] clientSideCodeToGenerateEncryptedData(String originalString, KeyPair keyPair) throws Exception{
       Cipher cipher = Cipher.getInstance("ECIES");
       cipher.init(Cipher.ENCRYPT_MODE, keyPair.getPublic());
       byte[] ecryptedBase64Data = Base64.encode(cipher.doFinal(originalString.getBytes("UTF-8")));
       return ecryptedBase64Data;

    }

   private static KeyPair serverSideKeyGeneration() throws Exception {
       Security.addProvider(new BouncyCastleProvider());

       KeyPairGenerator kpg = KeyPairGenerator.getInstance("ECIES");
       kpg.initialize(new ECGenParameterSpec("secp256r1"));

       // Key pair to store public and private key
       KeyPair keyPair = kpg.generateKeyPair();

       // System.out.println(keyPair.getPublic());
       // System.out.println(keyPair.getPrivate());

       return keyPair;

   }

   private static String decryptEncodedString(KeyPair keyPair, byte[] ret) throws Exception {
       Cipher iesCipherServer = Cipher.getInstance("ECIES");
       iesCipherServer.init(Cipher.DECRYPT_MODE, keyPair.getPrivate());
       String originalValue = new String(iesCipherServer.doFinal(Base64.decode(ret)));
       return originalValue;

   }
