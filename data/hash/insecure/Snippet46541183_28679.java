    public static String encoderByMd5(String str){
    MessageDigest md5;
    String newstr = "";
    try{
        md5 = MessageDigest.getInstance("MD5");
        BASE64Encoder encoder = new BASE64Encoder();
        try{
            newstr=encoder.encode(md5.digest(str.getBytes("utf-8")));
            newstr = newstr.replaceAll("=", "");
        }catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }catch(NoSuchAlgorithmException e) {
        e.printStackTrace();
    }
    return newstr;
}
