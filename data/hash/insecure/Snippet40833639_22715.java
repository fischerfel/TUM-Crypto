    public static String hashPassword(String password)
    {
        MessageDigest md=null;
        try {
            md=MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        String passwd=new String (md.digest(password.getBytes()));

        //To convert it to ',' so that it'll be removed with other ',''s
        passwd=passwd.replace('\n',',');
        passwd=passwd.replace(",","");
        return passwd;
    }
