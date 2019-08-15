import java.security.MessageDigest;

public class MD5Digest {

    public static void main(String[] args) throws Exception {

        //Copy and paste this section of code
        String original = "letmein";  //Replace "password" with the actual password inputted by the user
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(original.getBytes());
        byte[] digest = md.digest();
        StringBuffer sb = new StringBuffer();
        for (byte b : digest) {
            sb.append(String.format("%02x", b & 0xff));
        }
        //End copy/paste

        System.out.println("original:" + original);
        System.out.println("digested:" + sb.toString()); //sb.toString() is what you'll need to compare password strings
    }

}
