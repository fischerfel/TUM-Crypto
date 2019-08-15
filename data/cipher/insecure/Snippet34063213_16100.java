public String getPassCode() throws Exception {
        SecretKeySpec sks = null; 

        try {

            String encVal = "pass_code";

            if (encVal.isEmpty()) {
                return encVal;
            }


            sks = getDecryptKey();

            byte[] latDEC=null;


            Cipher c = Cipher.getInstance("AES");
            c.init(Cipher.DECRYPT_MODE, sks);

            latDEC = c.doFinal(Base64.decode(encVal, Base64.DEFAULT));




            return new String(latDEC);

        } catch (Exception e) {
            throw e;
        }
    }
