public static String encryptMaster(String message, String mykey) {

          String encrypted = "";
            String messageVal = message;
            String mykeyVal = mykey;

            SecretKey key = new SecretKeySpec(mykeyVal.getBytes(), "AES");
//          Utilities.showLogE("key", "" + key.toString());
            try {
                Cipher ecipher = Cipher.getInstance("AES");
                ecipher.init(Cipher.ENCRYPT_MODE, key);  
                byte[] utf8 = messageVal.getBytes("UTF-8");
                byte[] enc = ecipher.doFinal(utf8);
                //encrypted = Utilities.encodeBase64String(enc);
                encrypted = new BASE64Encoder().encode(enc);
//              Utilities.showLogE("encrypted", "" + encrypted);
            } catch (Exception e) {
//              Log.d(Constants.LOGKEY, "Exception");
                e.printStackTrace();
            }

            return encrypted;
    }
