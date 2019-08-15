public static void main(String[] args)  {
    PasswordEncoder pEncoder = new Md5PasswordEncoder(); 
    System.out.println(pEncoder.encodePassword("password", null));

    try {
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update("password".getBytes("UTF-8"));
        byte [] digest = md.digest();
        StringBuffer sb = new StringBuffer();
        for (byte b : digest) {
            sb.append(b);
        }
        System.out.println(sb.toString());

    } catch (NoSuchAlgorithmException e) {
        e.printStackTrace();
    } catch (UnsupportedEncodingException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    }
}
