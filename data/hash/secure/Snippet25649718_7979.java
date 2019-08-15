public class SHA256Algo {

    public static String createHash(String text) throws UnsupportedEncodingException, NoSuchAlgorithmException 
    {
        String encryptedText = "" ;
        MessageDigest md = MessageDigest.getInstance("SHA-512");

        md.update(text.getBytes("UTF-16")); // Change this to "UTF-16" if needed
        byte[] digest = md.digest();
        String str = digest.toString() ;
        return str ;
    }

    public static void main(String[] args) {
        try {
            System.out.println(createHash("tarun")) ;
            System.out.println(createHash("tarun")) ;
        } catch (UnsupportedEncodingException | NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
