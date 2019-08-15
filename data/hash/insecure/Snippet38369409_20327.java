String password = "spK47@wF";
    MessageDigest md;
    try {
        md = MessageDigest.getInstance("MD5");
        md.update(password.getBytes("UTF8"));
        byte[] digestedPwdBytes = md.digest();
        Base64 encoder = new Base64();
        System.out.print(new String(encoder.encode(digestedPwdBytes)) +":");
    } catch (NoSuchAlgorithmException e) {
        e.printStackTrace();
    } catch (UnsupportedEncodingException e) {
        e.printStackTrace();
    } 
