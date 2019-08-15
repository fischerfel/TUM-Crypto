import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import java.util.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;

class Test
{
    static void My_Get_Key() throws Exception
    {
    String temp;
    File f=new File("/home/mandar/Desktop/key.txt");
    Scanner sc=new Scanner(f);
    temp=sc.nextLine();

    byte[] sk=Base64.decode(temp);
    //byte[] sk=temp.getBytes();
    //byte[] sk=temp.getBytes(StandardCharsets.ISO_8859_1);
     SecretKey OriginalKey=new SecretKeySpec(sk,0,sk.length,"AES");
     System.out.println("Decrypt Key is "+OriginalKey.toString());
     //return OriginalKey;

    }
 static void My_Key_Generate() throws Exception
 {
KeyGenerator key=KeyGenerator.getInstance("AES");
key.init(128);
SecretKey sk=key.generateKey();
System.out.println("Encrypt Key is "+sk.toString());
BufferedWriter wt = new BufferedWriter(new FileWriter("/home/mandar/Desktop/key.txt"));
String KeyString =sk.toString();
    byte[] bytekey= KeyString.getBytes();
    String WriteKey= Base64.encode(bytekey);
wt.write(sk.toString()); 
wt.flush();
wt.close();
//return sk;

 }
  public static void main(String[] args) throws Exception
  {
    My_Key_Generate();
    My_Get_Key();
  }
 }
