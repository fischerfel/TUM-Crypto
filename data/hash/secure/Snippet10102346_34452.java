public static String SHA256(String text) throws  UnsupportedEncodingException  { 
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        byte[] shahash = new byte[40];
        md.update(text.getBytes("iso-8859-1"), 0, text.length());
        shahash = md.digest();
        return Base64.encodeToString(shahash, Base64.DEFAULT);
}
