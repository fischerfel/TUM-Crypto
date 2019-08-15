public static String encoderByMd5(String str){
    MessageDigest md5;
    String newstr = "";
    try{
        md5 = MessageDigest.getInstance("MD5");
        BASE64Encoder encoder = new BASE64Encoder();
        try{
            StringBuilder builder = new StringBuilder();
            for (byte b: md5.digest(str.getBytes("utf-8"))) {
                builder.append(String.format("%02X", b & 0xff));
            }

            newstr = encoder.encode(builder.toString().getBytes());
            newstr = newstr.replaceAll("=", "");
        }catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }catch(NoSuchAlgorithmException e) {
        e.printStackTrace();
    }
    return newstr.toUpperCase();
}
