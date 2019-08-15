public static SecretKeySpec genKey(String keyValue, String salt) {

        Utilities.showLogE("KEYSALT", "" + keyValue + " + salt);
        KeySpec keySpec = new PBEKeySpec((keyValue).toCharArray(), salt.getBytes(), 100, 128);
        SecretKeyFactory keyFactory = null;
        SecretKey secretKey = null;

        try {
            keyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            secretKey = keyFactory.generateSecret(keySpec);
            //Utilities.showLogE("secretKey.getEncoded()", "" + secretKey.getEncoded());
            //Utilities.showLogE("SecretKeySpec", "" + new SecretKeySpec(secretKey.getEncoded(), "AES"));
            return new SecretKeySpec(secretKey.getEncoded(), "AES");

        } catch (InvalidKeySpecException e) {
            Utilities.showLogE("Error in generating key -", e.toString());
            return null;
        } catch (NoSuchAlgorithmException e) {
            Utilities.showLogE("Error in generating key -", e.toString());
            return null;
        }
    }
