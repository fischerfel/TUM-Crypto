........
     private String SecretKey = "0123456789abcdef";**//Read the Simcard serial number and use the first 16 numbers as a SecretKey**

       public MCrypt()
        {
            ivspec = new IvParameterSpec(iv.getBytes());

            keyspec = new SecretKeySpec(SecretKey.getBytes(), "AES");

           ................
