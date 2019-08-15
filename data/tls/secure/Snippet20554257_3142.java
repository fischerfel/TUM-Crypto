 import java.net.*;
import java.io.*;

import javax.net.ssl.*;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.Principal;
import java.security.cert.*;

public class SSLSocketClient {

    private static String host;

    public static void main(String[] args) {
        String cipher = null;
        String portNo;
                int port = 0;
        boolean mykeystore = false;
        boolean chat = false;

        if (args.length == 5) {

            for (int i = 0; i < args.length; i++) {

                if (args[i].equals("-host")) {
                    host = args[++i];
                    continue;
                }
                if (args[i].equals("-port")) {
                    portNo = args[++i];
                    port = Integer.parseInt(portNo);
                    continue;
                }
                if (args[i].equals("-cipher")) {
                    cipher = args[++i];
                    continue;
                }
                if (args[i].equals("-chat")) {
                    chat = true;
                    continue;
                }

                if (args[i].equals("-mykeystore")) {
                    mykeystore = true;
                    continue;
                }
            }

        }

        else {
            System.out.println("Please check again parameter!");
        }

        if (mykeystore) {
            System.setProperty("javax.net.ssl.trustStore", "mykeystore");
            System.setProperty("javax.net.ssl.trustStorePassword", "kosuke");
        }

        SSLContext sc;
        try {
            sc = SSLContext.getInstance("TLSv1.2");
            sc.init(null, null, null);
            SSLSocketFactory factory = (SSLSocketFactory) sc.getSocketFactory();
        SSLSocket mysslsocket = (SSLSocket) factory.createSocket(host, port);

            if (cipher != null) {
                String[] cipherarray = { cipher };
                mysslsocket.setEnabledCipherSuites(cipherarray);
            }

            SSLSession session= mysslsocket.getSession();

            X509Certificate cert;


            // cert = (X509Certificate) session.getPeerCertificates()[0];

        //  System.out.println(session.getPeerHost() + "has presented a certificate belonging to: ");
            //Principal p=cert.getSubjectDN();




            //if(chat) {
                //BufferedReader in = new BufferedReader( new InputStreamReader(System.in));
                //BufferedWriter out = new BufferedWriter( new OutputStreamWriter( mysslsocket.getOutputStream()));

                //while(true) {
                    //String s= in.readLine();
                        //if (!s.equals("")) {
                            //out.write(s);
                            //out.write(" \r\n ");
                            //out.flush();
                            //if (s.equals(".")) break;
                    //  }
                //}
        //  }

            mysslsocket.startHandshake();
            mysslsocket.close();

        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (UnknownHostException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (KeyManagementException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
