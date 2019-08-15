public class App 
{   
public static void main(String[] args){
    String str = "helloWorldhelloWorldhelloWolrd";
    getHash(str);

}

public static void getHash(String str){
    try {
        byte[]  three = str.getBytes("UTF-8");
        MessageDigest   md = MessageDigest.getInstance("MD5");
        byte[] thedigest = md.digest(three);
        String  str1 = new String(thedigest,"UTF-8");
        System.err.println(str1);
    } catch (NoSuchAlgorithmException e) {

        e.printStackTrace();
    }catch (UnsupportedEncodingException e) {

        e.printStackTrace();
    }
}
