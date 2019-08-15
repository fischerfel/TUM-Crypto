/**
 * CURLs the page passed in for the HTML response. Uses AJAX so base domain must match the url.
 *
 * Special exception for MkItem requests
 *
 * @param url   - URL to CURL
 * @param debug - Pass in true for debug output.
 * @return HTML result as a string.
 * @throws IOException
 */
public static String getHTMLResponse(String url, boolean debug) throws IOException {
    if(debug){System.out.println("Making request to:\n\n" + url);}
    String body = "";

    try{
        // Create a trust manager that does not validate certificate chains
        TrustManager[] trustAllCerts = new TrustManager[] {new X509TrustManager() {
            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return null;
            }

            public void checkClientTrusted(X509Certificate[] certs, String authType) {
            }

            public void checkServerTrusted(X509Certificate[] certs, String authType) {
            }
        }
        };

        // Install the all-trusting trust manager
        SSLContext sc = SSLContext.getInstance("SSL");
        sc.init(null, trustAllCerts, new java.security.SecureRandom());
        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

        // Create all-trusting host name verifier
        HostnameVerifier allHostsValid = new HostnameVerifier() {
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        };

        // Install the all-trusting host verifier
        HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
    }catch (Exception e){
        Assert.fail("Exception thrown trying to perform GET request. " + e.getMessage());
    }
    URLConnection con;

    //If not a secure page, use cURL
        /*if(!url.contains("https://")){*/
        try{
            URL temp = new URL(url);
            con = temp.openConnection();
            InputStream in = con.getInputStream();
            String encoding = con.getContentEncoding();
            encoding = encoding == null ? "UTF-8" : encoding;
            body = IOUtils.toString(in, encoding);
        } catch (MalformedURLException e){
            Assert.fail("URL wasn't passed in correctly - " + e.getMessage());
        } catch (IOException e){
            throw e;
        }

    if(debug){ System.out.println("Body:\n"+ body); }

    Assert.assertNotNull("Body returned null for " + url, body);

    return body;
}
