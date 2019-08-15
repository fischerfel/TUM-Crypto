public static void main(String[] args){
        URL url;
        try {



            String imageUrl = "https://servername.com/communities/service/html/image?communityUuid=1e244250-6740-4949-aaac-682707a47099";
            String imageType = "image/png";

            String folder = "/Users/paulbastide/Desktop/";
            String fileName = "demo.png";
            File file = new File(folder + fileName);
            long fileLength = 0l;

            String userAgent = "Apache-HttpClient/4.3.3 (java 1.5)";
            String auth = "Basic =";

            url = new URL(imageUrl);
            HttpsURLConnection httpCon = (HttpsURLConnection) url.openConnection();
            httpCon.setDoOutput(true);

            //https://code.google.com/p/misc-utils/wiki/JavaHttpsUrl
            // Create a trust manager that does not validate certificate chains
            final TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {

                @Override
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
                @Override
                public void checkClientTrusted(
                        java.security.cert.X509Certificate[] arg0, String arg1)
                        throws CertificateException {
                    // TODO Auto-generated method stub

                }
                @Override
                public void checkServerTrusted(
                        java.security.cert.X509Certificate[] arg0, String arg1)
                        throws CertificateException {
                    // TODO Auto-generated method stub

                }
            } };

            // Install the all-trusting trust manager
            final SSLContext sslContext = SSLContext.getInstance( "SSL" );
            sslContext.init( null, trustAllCerts, new java.security.SecureRandom() );
            // Create an ssl socket factory with our all-trusting manager
            final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();
            httpCon.setSSLSocketFactory( sslSocketFactory );

            /**
             * adds the cookies
             */
            httpCon.setRequestProperty("Cookie", "");

            // Responds to two operations PUT and DELETE
            httpCon.setRequestMethod("PUT");

            httpCon.setRequestProperty("Content-Type", imageType );
            httpCon.setRequestProperty("slug", fileName);
            httpCon.setRequestProperty("Content-Length", "" + fileLength );
            httpCon.setRequestProperty("Content-Encoding", "binary");
            httpCon.setRequestProperty("User-Agent", userAgent);
            httpCon.setRequestProperty("Authorization", auth);

            byte[] fileBytes = FileUtils.readFileToByteArray( file);

            DataOutputStream out = new DataOutputStream(
                httpCon.getOutputStream());
            out.write(fileBytes);
            out.close();
            httpCon.getInputStream();

            System.out.println("The Response Code is " + httpCon.getResponseCode());

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }


    }
