 public static String encryptedLoginPassword( String password ) 
    {
        String encryptedData="";
    try{
        MessageDigest algorithm = MessageDigest.getInstance("MD5");
        byte[] defaultBytes = password.getBytes();
        algorithm.reset();
        algorithm.update(defaultBytes);
        byte messageDigest[] = algorithm.digest();

        StringBuffer hexString = new StringBuffer();
        for (int i=0;i<messageDigest.length;i++) {
            hexString.append(Integer.toHexString(0xFF & messageDigest[i]));
        }
        encryptedData=hexString.toString();
    }catch(NoSuchAlgorithmException nsae){

    }
    return encryptedData;
    }
