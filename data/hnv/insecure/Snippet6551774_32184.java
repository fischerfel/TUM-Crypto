    <%@ page language="java" import="
org.apache.http.HttpEntity,
org.apache.http.HttpResponse,
org.apache.http.auth.AuthScope,
org.apache.http.auth.UsernamePasswordCredentials,
org.apache.http.client.methods.HttpPost,
org.apache.http.client.methods.HttpGet,
org.apache.http.impl.client.DefaultHttpClient,
org.apache.http.util.EntityUtils,
java.io.InputStream,
java.io.InputStreamReader,
java.io.BufferedReader,
java.security.KeyStore,
java.io.FileInputStream,
java.io.File,
org.apache.http.conn.ssl.SSLSocketFactory,
org.apache.http.conn.scheme.Scheme,
javax.net.ssl.HostnameVerifier,
org.apache.http.impl.conn.SingleClientConnManager,
javax.net.ssl.HttpsURLConnection,
org.apache.http.conn.scheme.SchemeRegistry,
javax.net.ssl.SSLContext,
java.security.cert.X509Certificate,
javax.net.ssl.X509TrustManager,
javax.net.ssl.TrustManager,
org.apache.http.conn.ClientConnectionManager,
java.security.cert.CertificateException,
org.apache.http.conn.scheme.Scheme"
contentType="text/html; charset=ISO-8859-1"
pageEncoding="ISO-8859-1"%>

    <%
    String a_Url = request.getParameter( "url" ) ;

    DefaultHttpClient httpclient = new DefaultHttpClient();
    try {
        httpclient.getCredentialsProvider().setCredentials(
                new AuthScope(AuthScope.ANY_HOST, AuthScope.ANY_PORT, "realm"),
                new UsernamePasswordCredentials("test", "pass"));


        KeyStore trustStore  = KeyStore.getInstance(KeyStore.getDefaultType());
        //FileInputStream instream = new FileInputStream(new File("my.keystore"));
        InputStream instream = Thread.currentThread().getContextClassLoader().getResourceAsStream("my.keystore");
        try {
            trustStore.load(instream, "nopassword".toCharArray());
        } finally {
            try { instream.close(); } catch (Exception ignore) {}
        }
    /* 
        SSLSocketFactory socketFactory = new SSLSocketFactory(trustStore);
        Scheme sch = new Scheme("https", 443, socketFactory);
        httpclient.getConnectionManager().getSchemeRegistry().register(sch);
        */



        SSLContext ctx = SSLContext.getInstance("TLS");
        X509TrustManager tm = new X509TrustManager() {

        public void checkClientTrusted(X509Certificate[] xcs, String string) throws CertificateException {
        }

        public void checkServerTrusted(X509Certificate[] xcs, String string) throws CertificateException {
        }

        public X509Certificate[] getAcceptedIssuers() {
        return null;
        }
        };
        ctx.init(null, new TrustManager[]{tm}, null);
        SSLSocketFactory ssf = new SSLSocketFactory(ctx);
        ssf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
        ClientConnectionManager ccm = httpclient.getConnectionManager();
        SchemeRegistry sr = ccm.getSchemeRegistry();
        sr.register(new Scheme("https", ssf, 443));




        HttpGet httpget = new HttpGet(a_Url);



        System.out.println("executing request" + httpget.getRequestLine());
        HttpResponse res = httpclient.execute(httpget);

        HttpEntity entity = res.getEntity();

        System.out.println("----------------------------------------");
        System.out.println(res.getStatusLine());
        if (entity != null) {

            System.out.println("Response content length: " + entity.getContentLength());
            InputStream input = entity.getContent();
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));
            String ln = "";
            while((ln = reader.readLine()) != null) {
                out.println("During Get - " + ln);
            }
            entity.consumeContent();
        }
        EntityUtils.consume(entity);
    }

    catch (Throwable t) {
        StackTraceElement[] x = t.getStackTrace();
        for(int k=0;k<x.length;k++) {
            out.println(x[k].toString());
        }
        //out.println();
        t.printStackTrace();
    }


    finally {
        // When HttpClient instance is no longer needed,
        // shut down the connection manager to ensure
        // immediate deallocation of all system resources
        httpclient.getConnectionManager().shutdown();
    }


    %>
