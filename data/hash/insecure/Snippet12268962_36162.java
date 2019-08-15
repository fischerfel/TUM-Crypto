 private String createHash(String password){

    byte[] bytesOfMessage = null;
    try {
        bytesOfMessage = password.getBytes("UTF-8");
    } catch (UnsupportedEncodingException e1) {
        // TODO Auto-generated catch block
        e1.printStackTrace();
    }

    MessageDigest md = null;
    try {
        md = MessageDigest.getInstance("MD5");

    } catch (NoSuchAlgorithmException e1) {
        // TODO Auto-generated catch block
        e1.printStackTrace();
    }
    String thedigest = md.digest(bytesOfMessage).toString();
    System.out.println("passed in: "+thedigest);

    return thedigest;
}
