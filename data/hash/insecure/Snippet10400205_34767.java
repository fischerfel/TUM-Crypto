public static String stringToMD5(String password)
    {

        MessageDigest messageDigest;

        try {
            messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.update(password.getBytes(),0, password.length());  
            String hashedPass = new BigInteger(1,messageDigest.digest()).toString(16);  
            if (hashedPass.length() < 32) {
               hashedPass = "0" + hashedPass; 
            }
            return hashedPass;
        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }  
        return password;
    }
