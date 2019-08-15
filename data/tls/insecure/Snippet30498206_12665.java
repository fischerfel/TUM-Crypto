public class HttpsServer {
    public static void main(String[] args) {
    try {
        KeyStore ks = KeyStore.getInstance("PKCS12");
        ks.load(new FileInputStream("/opt/p12file.p12"), "p12pass".toCharArray());
        KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        kmf.init(ks, "p12pass".toCharArray());

        SSLContext sc = SSLContext.getInstance("TLS");
        sc.init(kmf.getKeyManagers(), null, null);

        SSLServerSocketFactory ssf = sc.getServerSocketFactory();
        SSLServerSocket s = (SSLServerSocket) ssf.createServerSocket(8080);

        while (true) {
            SSLSocket c = (SSLSocket) s.accept();
            BufferedWriter w = new BufferedWriter(new OutputStreamWriter(c.getOutputStream()));
            w.write("HTTP/1.0 200 OK");
            w.newLine();
            w.write("Content-Type: text/html");
            w.newLine();
            w.newLine();
            w.write("<html><body><h1>Https Server Works</h1></body></html>");
            w.newLine();
            w.flush();
            w.close();
            c.close();
        }
    }
    catch (Exception e) {
        e.printStackTrace();
    }
}
