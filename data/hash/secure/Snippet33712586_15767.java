import java.io.FileInputStream;
import java.security.MessageDigest;

public class SHAHash{
   public static void main(String[] args)throws Exception{
      MessageDigest md = MessageDigest.getInstance("SHA-256");
      FileInputStream fis = new FileInputStream("myfile");

      byte[] dataBytes = new byte[1024];
      int nread = 0;
      while((nread = fis.read(dataBytes))!= -1){
         md.update(dataBytes, 0, nread);
      };

      byte[] mdbytes = md.digest();

      StringBuffer sb1 = new StringBuffer();
      for(int i = 0; i < mdbytes.length; i++){
        sb1.append(Integer.toString((mdbytes[i] & 0xFF) + 0x100, 16).substring(1));
      }
      System.out.println("Hex format: " + sb1.toString());

      StringBuffer sb2 = new StringBuffer();
      for(int i = 0; i < mdbytes.length; i++){
         sb2.append(Integer.toHexString(0xFF & mdbytes[i]));
      }
      System.out.println("Hex format: " + sb2.toString());
   }
}
