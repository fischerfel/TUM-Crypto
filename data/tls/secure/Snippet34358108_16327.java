public class EchoServer {
    public static void main(String[] arstring) {
        try {
            final KeyStore keyStore = KeyStore.getInstance("JKS");

            final InputStream is = new FileInputStream("/Path/mySrvKeystore.jks");
            keyStore.load(is, "123456".toCharArray());
            final KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory .getDefaultAlgorithm());
            kmf.init(keyStore, "123456".toCharArray());
            final TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory .getDefaultAlgorithm());
            tmf.init(keyStore);

            SSLContext sc = SSLContext.getInstance("TLSv1.2");
            sc.init(kmf.getKeyManagers(), tmf.getTrustManagers(), new java.security.SecureRandom());

            SSLServerSocketFactory sslserversocketfactory = (SSLServerSocketFactory) SSLServerSocketFactory.getDefault();
            SSLServerSocket sslserversocket = (SSLServerSocket) sslserversocketfactory.createServerSocket(9997);
            SSLSocket sslsocket = (SSLSocket) sslserversocket.accept();
            sslsocket.setEnabledCipherSuites(sc.getServerSocketFactory().getSupportedCipherSuites());

            InputStream inputstream = sslsocket.getInputStream();
            InputStreamReader inputstreamreader = new InputStreamReader(inputstream);
            BufferedReader bufferedreader = new BufferedReader(inputstreamreader);

            String string = null;
            while ((string = bufferedreader.readLine()) != null) {
                System.out.println(string);
                System.out.flush();
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}
