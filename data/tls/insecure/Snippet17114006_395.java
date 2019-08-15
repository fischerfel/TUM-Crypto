public class SS {
    public SS() throws KeyStoreException, NoSuchAlgorithmException, CertificateException, KeyManagementException{
        try {
                /*
                System.setProperty("javax.net.ssl.trustStore", System.getProperty("user.dir")+"/src/cacerts.jks");
                System.setProperty("javax.net.ssl.trustStorePassword", "ja142236"); 
            */
                KeyStore ks = KeyStore.getInstance("JKS");
                ks.load(new FileInputStream(System.getProperty("user.dir")+"/src/cacerts.jks"), ("ahmed149").toCharArray());
                TrustManagerFactory kmf = TrustManagerFactory.getInstance("SunX509");
                kmf.init(ks);
                SSLContext sslcontext = SSLContext.getInstance("SSLv3");
                sslcontext.init(null, kmf.getTrustManagers(), null);

                SSLSocketFactory sf = (SSLSocketFactory) sslcontext.getSocketFactory();
                SSLSocket s = (SSLSocket) sf.createSocket("localhost",2149);
                s.startHandshake();

                DataOutputStream doo = new DataOutputStream(s.getOutputStream());

            DataInputStream di = new DataInputStream(s.getInputStream());

            doo.writeUTF(" Hiii From Client");

            System.out.println(di.readUTF());

            doo.close();
            di.close();
                        s.close();


        } catch (UnknownHostException ex) {
            Logger.getLogger(SS.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(SS.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void main(String[] args) throws KeyStoreException, NoSuchAlgorithmException, CertificateException, KeyManagementException{
        new SS();
    }
}
