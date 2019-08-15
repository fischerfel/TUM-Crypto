public static String encrypt(String password) {
    MessageDigest md;
    try {
        md = MessageDigest.getInstance("SHA");
        md.update(password.getBytes("UTF-8")); // step 3
        byte raw[] = md.digest(); // step 4
        String hash = (new BASE64Encoder()).encode(raw); // step 5
        return hash; // step 6
    } catch (NoSuchAlgorithmException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    } catch (UnsupportedEncodingException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    } 
    return null;
}
