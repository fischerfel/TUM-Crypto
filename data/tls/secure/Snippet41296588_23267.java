import java.net.*;
import java.io.*;
import java.util.*;
import javax.net.ssl.*;
import javax.net.*;
class Test{
  public static void main(String[] args){
    try{
      SSLContext context = SSLContext.getInstance("TLSv1.2");
      context.init(null,null,null);
      SSLServerSocketFactory serverSocketFactory = context.getServerSocketFactory();
      SSLServerSocket server = (SSLServerSocket)serverSocketFactory.createServerSocket(1024);
      server.setEnabledCipherSuites(server.getSupportedCipherSuites());
      SSLSocket socket = (SSLSocket)server.accept();
      DataInputStream in = new DataInputStream(socket.getInputStream());
      DataOutputStream out = new DataOutputStream(socket.getOutputStream());
      System.out.println(in.readInt());
    }catch(Exception e){e.printStackTrace();}
  }
}
