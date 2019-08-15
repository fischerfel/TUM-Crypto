import java.net.URL;
import java.security.KeyStore;
import java.security.SecureRandom;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;

...

    // assume keyStore is the KeyStore you read via getResourceAsStream
    final TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance("SunX509");
    trustManagerFactory.init(keyStore);

    final SSLContext context = SSLContext.getInstance("SSL");
    context.init(null, trustManagerFactory.getTrustManagers(), new SecureRandom());

    final URL url = new URL("https://whatever");
    final HttpsURLConnection urlConnection = (HttpsURLConnection) url.openConnection();
    urlConnection.setSSLSocketFactory(context.getSocketFactory());

...
