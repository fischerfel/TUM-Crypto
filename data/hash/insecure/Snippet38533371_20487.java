public class MD5Util {
    private static final String ALGORITHM = "MD5";
    public static String getHash(String text){
        try {
            MessageDigest md = MessageDigest.getInstance(ALGORITHM);
            md.update(text.getBytes());
            byte[] byteData = md.digest();
            StringBuffer hexString = new StringBuffer();
            for(byte bd : byteData){
                String hex = Integer.toHexString(bd & 0xff);
                if(hex.length() == 1)
                    hexString.append(0);
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(MD5Util.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}
