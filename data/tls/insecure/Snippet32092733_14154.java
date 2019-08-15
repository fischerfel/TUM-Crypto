import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import javax.net.ssl.*;

public class SSLServer {

    public static void main(String args[]){
        String ksname = "file.jks";
        char kspass[] = "pass".toCharArray();
        char ctpass[] = "pass".toCharArray();

        try {
            KeyStore ks = KeyStore.getInstance("JKS");
            ks.load(new FileInputStream(ksname), kspass);
            KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
            kmf.init(ks, ctpass);
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(kmf.getKeyManagers(), null, null);
            SSLServerSocketFactory ssf = sc.getServerSocketFactory();
            SSLServerSocket s = (SSLServerSocket) ssf.createServerSocket(4321);
            //printServerSocketInfo(s);
            SSLSocket c = (SSLSocket) s.accept();
            //printSocketInfo(c);

            BufferedReader r = new BufferedReader(new InputStreamReader(c.getInputStream()));
            BufferedWriter w = new BufferedWriter(new OutputStreamWriter(c.getOutputStream()));

            w.write("Server starts\n");

            w.flush();
            String k = null;
            while((k = r.readLine()) != null){
                //do something
                if(k.equals("end"))
                    break;
                w.write(resolve(k));
                w.newLine();
                w.flush();
            }
            w.close();
            r.close();
            c.close();
            s.close();

        } catch (KeyStoreException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (CertificateException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (UnrecoverableKeyException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (KeyManagementException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }   
    }

    private static void printSocketInfo(SSLSocket s) {
          System.out.println("Socket class: "+s.getClass());
          System.out.println("   Remote address = "
             +s.getInetAddress().toString());
          System.out.println("   Remote port = "+s.getPort());
          System.out.println("   Local socket address = "
             +s.getLocalSocketAddress().toString());
          System.out.println("   Local address = "
             +s.getLocalAddress().toString());
          System.out.println("   Local port = "+s.getLocalPort());
          System.out.println("   Need client authentication = "
             +s.getNeedClientAuth());
          SSLSession ss = s.getSession();
          System.out.println("   Cipher suite = "+ss.getCipherSuite());
          System.out.println("   Protocol = "+ss.getProtocol());
    }

    private static void printServerSocketInfo(SSLServerSocket s) {
          System.out.println("Server socket class: "+s.getClass());
          System.out.println("   Socket address = "
             +s.getInetAddress().toString());
          System.out.println("   Socket port = "
             +s.getLocalPort());
          System.out.println("   Need client authentication = "
             +s.getNeedClientAuth());
          System.out.println("   Want client authentication = "
             +s.getWantClientAuth());
          System.out.println("   Use client mode = "
             +s.getUseClientMode());
    }

    private static String resolve(String p){
        //some implementation
        return "something";
    }

    }
