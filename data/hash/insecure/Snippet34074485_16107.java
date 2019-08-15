import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.security.MessageDigest;

public class HMAC {

public static void main(String args[]) throws Exception {


  String datafile = "/Users/Samip/Desktop/crypto1";

MessageDigest md = MessageDigest.getInstance("SHA1");
FileInputStream fis = new FileInputStream(datafile);
byte[] dataBytes = new byte[1024];

int nread = 0; 

while ((nread = fis.read(dataBytes)) != -1) {
  md.update(dataBytes, 0, nread);
};

byte[] mdbytes = md.digest();

//convert the byte to hex format
StringBuffer sb = new StringBuffer("");



for (int i = 0; i < mdbytes.length; i++) {
    sb.append(Integer.toString((mdbytes[i] & 0xff) + 0x100, 16).substring(1));
}

//System.out.println("SHA-1 value is :: " + sb.toString());





    FileWriter file = new FileWriter("/Users/Samip/Desktop/crypto/output.txt");
    PrintWriter output = new PrintWriter(file);
    output.println(sb.toString());    
    output.close(); 
    System.out.println(sb.toString());

 }
 }
