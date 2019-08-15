public static void encrypt_AES(String message){

        Cipher ecipher;
        try {
            // generate secret key using DES algorithm
            SecretKeySpec key = new SecretKeySpec(theKey.getBytes("UTF-8"), "AES");

            ecipher = Cipher.getInstance("AES/ECB/PKCS7Padding");

            // initialize the ciphers with the given key
            ecipher.init(Cipher.ENCRYPT_MODE, key);

            byte[] encrypted = ecipher.doFinal(message.getBytes("UTF-8"));

        }catch (Exception e) {
            //    
            e.printStackTrace();
        }

    }
