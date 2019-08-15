//The below code found in http://www.logikdev.com/2010/11/01/encrypt-with-php-decrypt-with-java/
    public static String md5(String input) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] messageDigest = md.digest(input.getBytes());
        BigInteger number = new BigInteger(1, messageDigest);
        return number.toString(16);
    }

    public String decrypt(String encryptedData) {
        String decryptedData = null;
        try {
            SecretKeySpec skeySpec = new SecretKeySpec(md5("5A17K3Y").getBytes(), "AES");           
            String initialVectorString=md5(md5("5A17K3Y"));
            IvParameterSpec initialVector = new IvParameterSpec(initialVectorString.getBytes());
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding","SunJCE");            
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, initialVector);          
            encryptedData=encryptedData.replace('-','+').replace('_','/').replace(',','=');
            byte[] encryptedByteArray = (new org.apache.commons.codec.binary.Base64()).decode((encryptedData.getBytes()));          
            byte[] decryptedByteArray = cipher.doFinal(encryptedByteArray);
            decryptedData = new String(decryptedByteArray, "UTF8");
        } catch (Exception e) {         
            System.out.println("Error. Problem decrypting the data: " + e);
        }
    }
