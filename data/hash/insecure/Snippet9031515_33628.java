public class DigestRunner {

    /**
     * @param args
     * @throws NoSuchAlgorithmException 
     */
    public static void main(String[] args) throws NoSuchAlgorithmException {
        String password = "abcd";
        MessageDigest dig = MessageDigest.getInstance("MD5");

        System.out.println(toString(dig.digest(password.getBytes())));

    }


    public static String toString(byte[] ba)
    {
        StringBuilder hex = new StringBuilder(ba.length * 2);

        for(byte b : ba)
            hex.append(String.format("%02x", b));

        return hex.toString();
    }

}
