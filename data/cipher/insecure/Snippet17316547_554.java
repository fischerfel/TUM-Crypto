void encrypt(String inputText) throws Exception {
    try {
        String myKey = "mykey";
        byte[] mybyte = str.getBytes("ASCII");
        //String plainIV = "1234567890ABCDEF";
        KeySpec keySpec = new DESKeySpec(myKey.getBytes("ASCII"));
        SecretKey key = SecretKeyFactory.getInstance("DES").generateSecret(keySpec);
        //IvParameterSpec iv = new IvParameterSpec(org.apache.commons.codec.binary.Hex.decodeHex(plainIV.toCharArray()));
        IvParameterSpec iv = new IvParameterSpec(mybyte);
        Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE,key,iv);
        byte[] encoded = cipher.doFinal(inputText.getBytes("ASCII"));   
        System.out.println("Encoded Value ..... "+Base64.encodeBase64(encoded));
    } catch(UnsupportedEncodingException e) {
        System.out.println("Exception .. "+ e.getMessage());
    }
