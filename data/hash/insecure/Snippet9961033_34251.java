byte[] md5hash(char[] passwd) {
    String passwdtext = new String(passwd);
byte[] passdigest = null;
    try {
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        md5.reset();
        md5.update(passwdtext.getBytes("UTF-8"));
        passdigest = md5.digest();

    } catch (NoSuchAlgorithmException e) {

        e.printStackTrace();
    } catch (UnsupportedEncodingException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    }
    return passdigest;
}
