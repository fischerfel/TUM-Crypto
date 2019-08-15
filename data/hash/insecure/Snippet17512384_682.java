    String text = "abc";
    MessageDigest md = MessageDigest.getInstance("MD5");
    byte[] md5hash = new byte[32];
    try {
        md.update(text.getBytes("utf-8"), 0, text.length());
    } catch (UnsupportedEncodingException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    }
    md5hash = md.digest();  
