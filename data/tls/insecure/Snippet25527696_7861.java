    Finally I got the answer, Just need to import the certificate at run time and inject it while sending the request. It works for me.
    Code snippet is as follows:

    private HttpURLConnection getHTTPConnection(){
    TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return null;
                }


                public void checkClientTrusted(java.security.cert.X509Certificate[] certs, String authType) {
                }


                public void checkServerTrusted(java.security.cert.X509Certificate[] certs, String authType) {
                    System.out.println("authType is " + authType);
                    System.out.println("cert issuers");
                    for (int i = 0; i < certs.length; i++) {
                        System.out.println("\t" + certs[i].getIssuerX500Principal().getName());
                        System.out.println("\t" + certs[i].getIssuerDN().getName());
                    }
                }
            } };
            try {
                SSLContext sc = SSLContext.getInstance("SSL");
                sc.init(null, trustAllCerts, new java.security.SecureRandom());
                HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
                URL myurl = new URL("END_POINT");
                connection = (HttpURLConnection)myurl.openConnection();
                connection.setDoOutput(true);
                connection.setDoInput(true);
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-type", "text/xml; charset=utf-8");
                connection.setFollowRedirects(true);
                connection.setAllowUserInteraction(true);
            } catch (MalformedURLException mue) {
                mue.printStackTrace();
                System.exit(1);
            } catch (IOException ioe) {
                ioe.printStackTrace();
                System.exit(1);
            } catch (Exception e) {
                e.printStackTrace();
                System.exit(1);
            }
return connection;
              }
