import java.net.*;
import java.io.*;
import java.util.*;
import javax.net.ssl.*;
import javax.net.*;
class Test2{
  public static void main(String[] args){
    try{
      SSLContext context = SSLContext.getInstance("TLSv1.2");
      context.init(null,null,null);
      SSLSocketFactory socketFactory = context.getSocketFactory();
      SSLSocket socket = (SSLSocket)socketFactory.createSocket("localhost", 1024);
      socket.setEnabledCipherSuites(socket.getSupportedCipherSuites());
      DataInputStream in = new DataInputStream(socket.getInputStream());
      DataOutputStream out = new DataOutputStream(socket.getOutputStream());
      out.writeInt(1337);     
    }catch(Exception e){e.printStackTrace();}
  }
}
