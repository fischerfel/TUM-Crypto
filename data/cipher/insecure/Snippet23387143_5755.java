public static String encryptPassword(String pass) {

        public static final String DESKEY = "REPPIFY_ABCDEFGHIJKLMNOPQRSTUVWXYZ";
            System.out.println("Here is my password = "+pass);
        DESKeySpec keySpec = null;
        SecretKeyFactory keyFactory = null;
        SecretKey key = null;
        Cipher cipher = null;
        BASE64Encoder base64encoder = new BASE64Encoder();

        byte[] cleartext = null;
        String encrypedPwd = null;
            String pass = "ankit@123";

        try {
            keySpec = new DESKeySpec(DESKEY.getBytes("UTF8"));
            keyFactory = SecretKeyFactory.getInstance("DES");
            key = keyFactory.generateSecret(keySpec);
            if(pass!=null) {
                cleartext = pass.getBytes("UTF8");
                cipher = Cipher.getInstance("DES");
                cipher.init(Cipher.ENCRYPT_MODE, key);
                encrypedPwd = base64encoder.encode(cipher.doFinal(cleartext));
            }
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } // cipher is not thread safe 
        catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        }
        System.out.println("Here I am printing encrypted pwd = "+encrypedPwd);
        return encrypedPwd;
    }
