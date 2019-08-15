 public static String encryptString(String str){
        char[] CHARSET_AZ_09 = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".toCharArray();
        String initialVectorString = Helper.randomString(CHARSET_AZ_09, 16);
        byte[] vectorbytes = (new org.apache.commons.codec.binary.Base64()).encode(initialVectorString.getBytes());

        String encryptedString = Helper.encrypt(str, initialVectorString, "mykey");
        return encryptedString;
    }
    public static String randomString(char[] characterSet, int length) {
        Random random = new SecureRandom();
        char[] result = new char[length];
        for (int i = 0; i < result.length; i++) {
            // picks a random index out of character set > random character
            int randomCharIndex = random.nextInt(characterSet.length);
            result[i] = characterSet[randomCharIndex];
        }
        String str = new String(result);
        try{
            str = new String(str.getBytes(),"UTF8");
        }
        catch(Exception e){

        }
        Log.d("check",str.getBytes().length+"");
        return str;
    }
    public static String encrypt(String data,String initialVectorString,String secretKey){
        String encryptedString = null;
        try{
             SecretKeySpec skeySpec = new SecretKeySpec(md5(secretKey).getBytes(), "AES");
             IvParameterSpec initialVector = new IvParameterSpec((new org.apache.commons.codec.binary.Base64()).decode(initialVectorString.getBytes()));
             Cipher cipher = Cipher.getInstance("AES/CFB8/NoPadding");
             cipher.init(Cipher.ENCRYPT_MODE, skeySpec, initialVector);
             byte[] plainTextByteArray = (new org.apache.commons.codec.binary.Base64()).decode(data.getBytes());
             byte[] encryptedByteArray = cipher.doFinal(plainTextByteArray);
             encryptedString = new String(encryptedByteArray, "UTF8");
        }
        catch (Exception e) {
            Log.d("Problem decrypting the data", e.getLocalizedMessage());
        }
        return encryptedString;
    }
