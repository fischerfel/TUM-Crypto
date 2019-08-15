    public static String hashPassword(String password) {
       MessageDigest mdEnc = null;
       try {
         mdEnc = MessageDigest.getInstance("SHA-256");
       }  catch (NoSuchAlgorithmException e) {
        return null;
       }
       mdEnc.update(password.getBytes(), 0, password.length());

       return new BigInteger(1, mdEnc.digest()).toString(16);
}
