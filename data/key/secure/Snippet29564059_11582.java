 public static String encryptKey(String key,String text) throws CryptoException 
    {
        String key1=null;
        try {

            System.out.println("input parameters length  "+key.length()+ " " +text.length());
            System.out.println("input parameters value "+key+ " " +text);
            Key secretKey = new SecretKeySpec(key.getBytes(), ALGORITHM);
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);

            cipher.init(Cipher.ENCRYPT_MODE, secretKey);             
            byte[] inputBytes=text.getBytes();
           //System.out.println(inputBytes.length);
            byte[] outputBytes = cipher.doFinal(inputBytes);
           // key= WriteArray.bytesToString(outputBytes);  
           key1=outputBytes.toString();


            System.out.println("output parameters  "+key1.length()+" "+ text.length());
            System.out.println("output parameters value "+key1+ " " +text);
        } 
