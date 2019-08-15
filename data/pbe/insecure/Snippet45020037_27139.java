 public String encrypt(String word) throws Exception {

        byte[] ivBytes;
        String password="Hello"; 
    /*you can give whatever you want for password. This is for testing purpose*/

        SecureRandom random = new SecureRandom();
        byte bytes[] = new byte[20];
        random.nextBytes(bytes);
        byte[] saltBytes = bytes;

        // Derive the key
       SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");

        PBEKeySpec spec = new PBEKeySpec(password.toCharArray(),saltBytes,65556,256);

         SecretKey secretKey = factory.generateSecret(spec);
        SecretKeySpec secret = new SecretKeySpec(secretKey.getEncoded(), "AES");

         //encrypting the word

         Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
         cipher.init(Cipher.ENCRYPT_MODE, secret);
         AlgorithmParameters params = cipher.getParameters();
         ivBytes =   params.getParameterSpec(IvParameterSpec.class).getIV();

         byte[] encryptedTextBytes =  cipher.doFinal(word.getBytes("UTF-8"));

         //prepend salt and vi

          byte[] buffer = new byte[saltBytes.length + ivBytes.length + encryptedTextBytes.length];

          System.arraycopy(saltBytes, 0, buffer, 0, saltBytes.length);
          System.arraycopy(ivBytes, 0, buffer, saltBytes.length, ivBytes.length);

           System.arraycopy(encryptedTextBytes, 0, buffer, saltBytes.length + ivBytes.length, encryptedTextBytes.length);

           return new Base64().encodeToString(buffer);

        }
