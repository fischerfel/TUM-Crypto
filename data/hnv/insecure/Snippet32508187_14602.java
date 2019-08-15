import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;

    try {
        // The following SSL code is temporary and allows access to the IIS Server that has a self-signed certificate

        // Setup a custom SSL Factory object which simply ignore the certificates
        // validation and accept all type of self signed certificates
        SSLSocketFactory sslFactory = new SimpleSSLSocketFactory(null);
        sslFactory.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

        // Enable HTTP parameters
        HttpParams params = new BasicHttpParams();
        HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
        HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);

        // Register the HTTP and HTTPS Protocols. For HTTPS, register our custom SSL Factory object.
        SchemeRegistry registry = new SchemeRegistry();

        registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
        registry.register(new Scheme("https", sslFactory, 443));

        // Create a new connection manager using the newly created registry and then create a new HTTP client
        // using this connection manager
        ClientConnectionManager ccm = new ThreadSafeClientConnManager(params, registry);
        DefaultHttpClient httpClient = new DefaultHttpClient(ccm, params);
        HttpGet request = new HttpGet();

        URI uri = new URI(resources.getString("http://www.jazzradio.fr/api-docs"));
        request.setURI(uri);
        HttpResponse response = httpClient.execute(request);


        if (response.getStatusLine().toString().equalsIgnoreCase(HTTP_STATUS_OK)) {
            isRestfulServiceAvailable = true;

            bufferedReader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

            // JSON data is in bufferedReader
            // Get from bufferedReader to JSONArray, then from JSONArray to ArrayList

            String line = bufferedReader.readLine();
            stringBuilder.append(line);

            ...
