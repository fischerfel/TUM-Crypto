import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;

import com.caucho.burlap.client.BurlapProxyFactory;

public class NoSslCertificateCheckBurlapProxyFactory 
        extends BurlapProxyFactory {

    private final HostnameVerifier hostnameVerifier;

    public NoSslCertificateCheckBurlapProxyFactory() {
        hostnameVerifier = new NoCheckHostnameVerifier();
    }

    @Override
    protected URLConnection openConnection(final URL url) throws IOException {
        final URLConnection connection = super.openConnection(url);

        if (connection instanceof HttpsURLConnection) {
            final HttpsURLConnection httpsURLConnection = 
                (HttpsURLConnection) connection;
            httpsURLConnection.setHostnameVerifier(hostnameVerifier);
        }

        return connection;
    }
}
