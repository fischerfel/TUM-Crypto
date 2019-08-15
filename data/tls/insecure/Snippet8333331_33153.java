TrustManager tm = new X509TrustManager() {
public void checkClientTrusted(X509Certificate[] chain,
                String authType)
                throws CertificateException {
    System.out.println("yay1");
}



public void checkServerTrusted(X509Certificate[] chain,
                String authType)
                throws CertificateException {
    System.out.println("yay2");
}

public X509Certificate[] getAcceptedIssuers() {
    throw new UnsupportedOperationException("Not supported yet.");
}
};


String uri = "http://127.0.0.1:8083/SoapContext/SoapPort";
Object implementor = new Main();

Endpoint endpoint = Endpoint.create(implementor);

SSLContext ssl = SSLContext.getInstance("TLS");

KeyManagerFactory keyFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
KeyStore store = KeyStore.getInstance("JKS");

store.load(new FileInputStream("serverkeystore"),"123456".toCharArray());

keyFactory.init(store, "123456".toCharArray());


TrustManagerFactory trustFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());

trustFactory.init(store);


ssl.init(keyFactory.getKeyManagers(),
new TrustManager[] { tm }, null);

HttpsConfigurator configurator = new HttpsConfigurator(ssl);

HttpsServer httpsServer = HttpsServer.create(new InetSocketAddress(8083), 8083);

httpsServer.setHttpsConfigurator(configurator);

HttpContext httpContext = httpsServer.createContext("/SoapContext/SoapPort");

httpsServer.start();

endpoint.publish(httpContext);
