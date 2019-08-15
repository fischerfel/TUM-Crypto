  private String checkHostName(String hostUserName) throws IOException, InvalidKeyException, InvalidAlgorithmParameterException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {
            String deCrypted = null;
            FileInputStream is = new FileInputStream(new File("C:\\test\\SSH\\PrivateKey\\keystore.properties"));
            Properties properties = new Properties();
            properties.load(is);
            ssh_Public_Private = new SSH_Public_Private();
            boolean isHostNameExist = false;
            if (properties.getProperty(hostUserName) == null) {

                OutputStream outputStream = new FileOutputStream(
                        "C:\\test\\SSH\\PrivateKey\\keystore.properties");
                String passPhraseStored = new String(enCryptPwd());
                properties.setProperty(hostUserName,passPhraseStored );
                properties.store(outputStream, null);
                outputStream.close();
                is.close();
                return checkHostName(hostUserName);
            }else{
                System.out.println(properties.getProperty(hostUserName));
                String passPhrase = properties.getProperty(hostUserName);
                 deCrypted = deCryptPwd(passPhrase);            //isHostNameExist = true;
            }
            return deCrypted;

        }

My encryption and decryption piece of code is as follow :

    private static String enCryptPwd() throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
        String decrypted = null;
        byte[] encrypted = null;
        try {
            String text = "";
            Scanner sc = new Scanner(System.in);
            System.out.println("Enter your passphrase : " );
            text = sc.next();
            String key = "Bar12345Bar12345"; // 128 bit key
            //String key = "AesSEcREtkeyABCD";
            // Create key and cipher
            Key aesKey = new SecretKeySpec(key.getBytes("UTF-8"), "AES");
            System.out.println(aesKey.getFormat());
            Cipher cipher = Cipher.getInstance("AES");
            // encrypt the text
            cipher.init(Cipher.ENCRYPT_MODE, aesKey);
            encrypted = cipher.doFinal(text.getBytes("UTF-8"));
            System.err.println(new String(encrypted));
            System.err.println(encrypted.length);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return new String(encrypted);
    }

    private static  String deCryptPwd(String encrypted) throws InvalidKeyException, InvalidAlgorithmParameterException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {
        String originalString = "";
        try {
            String key = "Bar12345Bar12345"; // 128 bit key
            //String key = "AesSEcREtkeyABCD";
            // Create key and cipher
            Key aesKey = new SecretKeySpec(key.getBytes("UTF-8"), "AES");
            Cipher cipher = Cipher.getInstance("AES");
            // decrypt the text
            cipher.init(Cipher.DECRYPT_MODE, aesKey);
            byte[] encryptBytes = new byte[encrypted.length()];
            encryptBytes = encrypted.getBytes();
            byte[] decrypted = cipher.doFinal(encryptBytes);
            originalString = new String(decrypted, "UTF-8");
            System.out.println(originalString);
            System.err.println(decrypted);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return originalString;
      }
