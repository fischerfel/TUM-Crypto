private static String passwordEncryption(String oldPass){
    String newPass = "";
    try {
        MessageDigest messageDigest = MessageDigest.getInstance("MD5");  
        messageDigest.update(oldPass.getBytes(), 0, oldPass.length());  
        newPass = new BigInteger(1,messageDigest.digest()).toString(16);  
        if (newPass.length() < 32) {
            newPass = "0" + newPass; 
        }
        return newPass;
    } catch (NoSuchAlgorithmException e) {
        e.printStackTrace();
    }
    return newPass;

}
