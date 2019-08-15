package XMailMessenger;

public class DummySSLSocketFactory extends SSLSocketFactory {
private SSLSocketFactory factory;

public DummySSLSocketFactory() {
try {


    SSLContext sslcontext = SSLContext.getInstance("TLS");
    //Security.removeProvider("SunJSSE");
    sslcontext.init(null,
             new TrustManager[] { new DummyTrustManager()},
            null );
    factory = (SSLSocketFactory)sslcontext.getSocketFactory();

} catch(Exception ex) {
    System.out.println(ex.toString());
}
}

public static SocketFactory getDefault() {
    SocketFactory a = new DummySSLSocketFactory();
    if ( a == null ) { System.out.println("1"); }
    return a;
}
 ...
