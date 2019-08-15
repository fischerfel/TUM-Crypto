public static final byte[] md5(String s) { 
    try { 

        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] messageDigest = md.digest(s.getBytes("UTF-8"));

        String md5 = EncodingUtils.getString(messageDigest, "UTF-8");

        Log.i("Function MD5", md5);
        Log.i("Function MD5 Length","Length: "+ md5.length());

        return messageDigest;

    } catch (NoSuchAlgorithmException e) {
        e.printStackTrace();
    } catch (UnsupportedEncodingException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    } 
    return null;
}   
