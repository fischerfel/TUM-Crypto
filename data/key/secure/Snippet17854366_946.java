    public  String Encrypt(String text, String key)
        throws Exception {

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        byte[] keyBytes= new byte[16];
        byte[] b= key.getBytes("UTF-8");
        int len= b.length;
        if (len > keyBytes.length) len = keyBytes.length;
        System.arraycopy(b, 0, keyBytes, 0, len);
        SecretKeySpec keySpec = new SecretKeySpec(keyBytes,0,keyBytes.length, "AES");

        cipher.init(Cipher.ENCRYPT_MODE,keySpec, ivspec);

            byte[] outputBytes = new byte[100];
            byte[] inputBytes;
            inputBytes=text.getBytes("UTF-8");
        int results = cipher.doFinal(inputBytes,0,inputBytes.length,outputBytes,0);

            String str = new String(outputBytes, 0, results);

        String strMobile_No = Base64.encode(str.getBytes());
            String strresult=strMobile_No.toString();
          textField.setString(strMobile_No);
          return strresult;

        }
