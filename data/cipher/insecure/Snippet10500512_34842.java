public static void main(String[] args) throws Exception {



        String key="this is key";

        String message="This is just an example";

           KeyGenerator kgen = KeyGenerator.getInstance("AES");

           kgen.init(128, new SecureRandom(Base64.decodeBase64(key)));

           // Generate the secret key specs.
           SecretKey skey = kgen.generateKey();

           byte[] raw = skey.getEncoded();

           SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");

           Cipher cipher = Cipher.getInstance("AES");

           cipher.init(Cipher.ENCRYPT_MODE, skeySpec);

           byte[] encrypted= cipher.doFinal(Base64.decodeBase64(message));

           String encryptedString=Base64.encodeBase64String(encrypted);

           cipher.init(Cipher.DECRYPT_MODE, skeySpec);

           byte[] original =
             cipher.doFinal(Base64.decodeBase64(encryptedString));

           System.out.println(Base64.encodeBase64String(original));


    }
