public class Locator
{
    private final String url;
    private String host;

    public Locator(String host, int port)
    {
        url = "https://" + host + ":" + port + "/rest/terminal_location/location?query=";
        this.host = host;
    }

    protected static TrustManager[] getTrustManagers(String trustStoreType, InputStream trustStoreFile) throws Exception {
        KeyStore trustStore = KeyStore.getInstance(trustStoreType);
        trustStore.load(trustStoreFile, null);
        TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        tmf.init(trustStore);
        return tmf.getTrustManagers();
    }

    public void GetPhoneLocation(String phoneNumber, int acceptableAccuracy, int requestedAccuracy, Tolerance tolerance, int maxAge, int responseTime)
    {
        String charset = "UTF-8";
        String rawQuery = String.format(getLocationQuery(phoneNumber, acceptableAccuracy, requestedAccuracy, tolerance, maxAge, responseTime));
        DefaultHttpClient httpClient = new DefaultHttpClient();
        httpClient.getCredentialsProvider().setCredentials(
                new AuthScope("https://openmiddleware.pl", 443),
                new UsernamePasswordCredentials("user", "pass"));
        try
        {
            String encodedQuery = URLEncoder.encode(rawQuery, charset);
            SSLContext ctx = SSLContext.getInstance("SSL");
            TrustManager[] trustManagers = getTrustManagers("jks", new FileInputStream(new File("/Users/pmichna/Desktop/my.truststore")));
            ctx.init(null, trustManagers, null);
            SSLSocketFactory factory = new SSLSocketFactory(ctx, new      StrictHostnameVerifier());

            ClientConnectionManager manager = httpClient.getConnectionManager();
            manager.getSchemeRegistry().register(new Scheme("https", 443, factory));
            HttpGet httpget = new HttpGet(url + encodedQuery);

            System.out.println("executing request" + httpget.getRequestLine());

            HttpResponse response = httpClient.execute(httpget);
            HttpEntity entity = response.getEntity();

            System.out.println("----------------------------------------");
            System.out.println(response.getStatusLine());
            System.out.println(EntityUtils.toString(entity));
            if (entity != null) {
                System.out.println("Response content length: " + entity.getContentLength());
            }
            EntityUtils.consume(entity);
        } catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (Exception e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        finally
        {
            httpClient.getConnectionManager().shutdown();
        }
    }

    public enum Tolerance
    {
        //[...]
    }

    private String getLocationQuery(String phoneNumber, int acceptableAccuracy, int requestedAccuracy, Tolerance tolerance, int maxAge, int responseTime)
    {
        //[...]
    }
}
