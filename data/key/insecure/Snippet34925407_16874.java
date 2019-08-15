   public static void setKey(String myKey) {

        MessageDigest sha = null;
        try {
            key = new byte[]{(byte) '5', (byte) 'F', (byte) '8', (byte) 'p', (byte) 'J', (byte) 't', (byte) 'v', (byte) 'U', (byte) 'm', (byte) 'q', (byte) 'k', (byte) '7', (byte) 'A', (byte) 'M', (byte) 'v', (byte) 'b', (byte) 'q', (byte) 'o', (byte) 'H', (byte) 'M', (byte) '9', (byte) 'a', (byte) 'p', (byte) '4', (byte) '9', (byte) 'm', (byte) 'c', (byte) 'u', (byte) 'u', (byte) '5', (byte) 'B', (byte) 'X'};
            System.out.println(new String(key, "UTF-8"));
            secretKey = new SecretKeySpec(key, "AES");
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


    public static String encrypt(String strToEncrypt) {
        try {
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            setEncryptedarr(cipher.doFinal(strToEncrypt.getBytes("UTF-8")));
            setEncryptedString(String.valueOf(Base64.encode(cipher.doFinal(strToEncrypt.getBytes("UTF-8")), Base64.DEFAULT)));
            //setEncryptedString(Base64.encodeBase64String(cipher.doFinal(strToEncrypt.getBytes("UTF-8"))));
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error while encrypting: " + e.toString());
        }
        return null;
    }

    public static String decryptbyte(byte[] strToDecrypt) {
        try {
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            setDecryptedarr(cipher.doFinal(strToDecrypt));
            System.out.println("encrypt : decropted size : " + getDecryptedarr().length);
            setDecryptedString(new String(cipher.doFinal(strToDecrypt)));
        } catch (Exception e) {
            System.out.println("Error wnhile decrypting: " + e.toString());
            e.printStackTrace();
        }
        return null;
    }

    public static String decrypt(String strToDecrypt) {
        try {
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            setDecryptedarr(cipher.doFinal(Base64.decode(strToDecrypt, Base64.DEFAULT)));
            setDecryptedString(new String(cipher.doFinal(Base64.decode(strToDecrypt, Base64.DEFAULT))));

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error while decrypting: " + e.toString());

        }
        return null;
    }
