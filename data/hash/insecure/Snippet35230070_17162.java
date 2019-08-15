public class BruteForce {
static int num_bytes=24;
static String rand = "";
static String s = "aefbcefacefeacaecefc";
static byte[] random = null;
static byte[] md = null;

public static void main(String[] args) throws Exception{
    md = mini_md5_bytes(s, num_bytes);
    if(s.equalsIgnoreCase(rand)){
        System.out.println(rand);
    }
    else{
        rand = brute(md, s);
    }

}




public static byte[] mini_md5_bytes(String s, int num_bytes){

    byte[] md = md5_bytes(s);

    return Arrays.copyOf(md,num_bytes);

}
public static byte[] md5_bytes(String s){

     MessageDigest md;
     try {
         md = MessageDigest.getInstance("MD5");
         md.update(s.getBytes());
         return md.digest();
     } catch( java.security.NoSuchAlgorithmException e) {
         return null;
     }
 }
public static String brute(byte[] md, String s) throws Exception{
    while(!s.equalsIgnoreCase(rand)){
        rand =  RandomStringGenerator.generateRandomString(20,RandomStringGenerator.Mode.ALPHA);
        byte[] random = mini_md5_bytes(rand, num_bytes);
            if((Arrays.equals(random, md))){
                rand = s;
                return rand;
            }               
    }
    return null;

   }


 }
