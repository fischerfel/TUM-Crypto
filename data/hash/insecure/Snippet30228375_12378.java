import java.security.MessageDigest;

public class TransCode{
public static byte[] transcode(String text) throws Exception{
    byte[] bytes = text.getBytes("UTF-8");
    return bytes;
}

public static void main(String[] args){
    try{
        System.out.println("ORIGIN STRING: hello world");
        byte[] byteArray = TransCode.transcode("hello world");
        int arrLen = byteArray.length;
        MessageDigest messageDigest = MessageDigest.getInstance("MD5");
        messageDigest.update(byteArray);
        System.out.print("BYTE ARRAY:  ");
        for (int i=0;i<arrLen;i++){
            System.out.print(byteArray[i]);
            System.out.print("  ");
        }
        System.out.println();
        byteArray = messageDigest.digest();
        System.out.print("MD5 RESULT:  ");
        for (int i=0;i<arrLen;i++){
            System.out.print(byteArray[i]);
            System.out.print("  ");
        }
        System.out.print("\n");
    }
    catch(Exception ex){
        System.out.println(ex);
    }
}
