MessageDigest md = null;
    try {
        md = MessageDigest.getInstance("MD5");
    } catch (NoSuchAlgorithmException e3) {
        // TODO Auto-generated catch block
        e3.printStackTrace();
    }
    try {
        md.update(password.getBytes("UTF-8"));
    } catch (UnsupportedEncodingException e3) {
        // TODO Auto-generated catch block
        e3.printStackTrace();
    }
    byte raw[] = md.digest();
