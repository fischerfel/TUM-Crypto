 import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class sha1 {

    /**
     * @param args
     * @throws NoSuchAlgorithmException 
     */
    public String sb1;

    public void printkey(String convert){

        sb1=sha1(convert) ;
        System.out.println(sb1);
    }


    public String sha1(String input)  {
        StringBuffer sb = new StringBuffer();
        sha1 rr= new sha1();
        try{

            MessageDigest mDigest = MessageDigest.getInstance("SHA1");
            byte[] result = mDigest.digest(input.getBytes());

            for (int i = 0; i < result.length; i++) {
            sb.append(Integer.toString((result[i] & 0xff) + 0x100, 16).substring(1));

        }

        rr.sb1=sb.toString();
        System.out.println("\n");
        System.out.println(rr.sb1);



        }
        catch ( NoSuchAlgorithmException nsae ) { 
            System.out.println(nsae);
        }
    return sb1;
    }
