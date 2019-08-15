String myData = "kgxCSfBSw5BRxmjgc4qYhwN12dxG0dyf=";
        byte[] salt = new byte[] {0x49, 0x76, 0x61, 0x6E, 0x20, 0x4D, 0x65, 0x64, 0x76, 0x65, 0x64, 0x65, 0x76};
        String pw  = "kmjfds(#1231SDSA()#rt32geswfkjFJDSKFJDSFd";

        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        PBEKeySpec pbeKeySpec = new PBEKeySpec(pw.toCharArray(), salt, 1000, 384);
        Key secretKey = factory.generateSecret(pbeKeySpec);
        byte[] key = new byte[32];
        byte[] iv = new byte[16];
        System.arraycopy(secretKey.getEncoded(), 0, key, 0, 32);
        System.arraycopy(secretKey.getEncoded(), 32, iv, 0, 16);


        SecretKeySpec secretSpec = new SecretKeySpec(key, "AES");
        AlgorithmParameterSpec ivSpec = new IvParameterSpec(iv);
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        Cipher cipher1 = Cipher.getInstance("AES/CBC/PKCS5Padding");

        try {
            cipher.init(Cipher.DECRYPT_MODE,secretSpec,ivSpec);
            cipher1.init(Cipher.ENCRYPT_MODE,secretSpec,ivSpec);
        } catch (InvalidKeyException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        //byte[] decordedValue;
        //decordedValue = new BASE64Decoder().decodeBuffer(myData);
        //decordedValue = myData.getBytes("ISO-8859-1");
          //byte[] decValue = cipher.doFinal(myData.getBytes());
        //Base64.getMimeEncoder().encodeToString(cipher.doFinal(myData.getBytes()));
            //String decryptedValue = new String(decValue);
        byte[] decodedValue  = new Base64().decode(myData.getBytes());



          String clearText = "ljfva09876FK";


          //String encodedValue  = new Base64().encodeAsString(clearText.getBytes("UTF-16"));



          byte[] cipherBytes = cipher1.doFinal(clearText.getBytes("UTF-16LE"));
          //String cipherText = new String(cipherBytes, "UTF8");
          String encoded = Base64.encodeBase64String(cipherBytes);
          System.out.println(encoded);


            byte[] decValue =    cipher.doFinal(decodedValue);

            System.out.println(new String(decValue, StandardCharsets.UTF_16LE));
