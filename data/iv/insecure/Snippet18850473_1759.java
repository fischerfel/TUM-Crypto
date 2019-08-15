public static byte[] enc(byte[] input, byte[] keyStr){
        byte[] output = null;

        try {           
            byte[] newKey = getByteArrays(keyStr, 0, 32);
            SecretKey secretKey = new SecretKeySpec(newKey, "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");  
            //Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
            String ivStr = "";
            byte[] ivKey = getByteArrays(ivStr.getBytes("UTF-8"), 0, 16);
            IvParameterSpec ips = new IvParameterSpec(ivKey);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, ips, null);         
            output = cipher.doFinal(input);

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }       
        return output;
    }
