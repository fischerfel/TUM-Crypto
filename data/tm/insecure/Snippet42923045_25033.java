import java.net.Socket;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509ExtendedTrustManager;

class AnyTrust extends X509ExtendedTrustManager {
    static final X509Certificate[] ANY_CA = {};
    @Override public X509Certificate[] getAcceptedIssuers() { return ANY_CA; }
    @Override public void checkServerTrusted(final X509Certificate[] c, final String t)  {}
    @Override public void checkClientTrusted(final X509Certificate[] c, final String t)  { }
    @Override public void checkServerTrusted(final X509Certificate[] c, final String t, final SSLEngine e)  { }
    @Override public void checkServerTrusted(final X509Certificate[] c, final String t, final Socket    e)  { }
    @Override public void checkClientTrusted(final X509Certificate[] c, final String t, final SSLEngine e)  { }
    @Override public void checkClientTrusted(final X509Certificate[] c, final String t, final Socket    e)  { }
}

public class S1 {
    public static final void main(final String [] argc) throws Throwable {
        final SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(null, new TrustManager[]{new AnyTrust()}, null);
        final SSLSocketFactory sslsocketFactory = sslContext.getSocketFactory();
    }
}
