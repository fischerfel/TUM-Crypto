    public class HPOMiUtil {
        private static final Log LOG = LogFactory.getLog(HPOMiUtil.class);
        private Map<String, SynchBackUrlDTO> httpPostFactory;
        private String pathKeyStore;
        private String passwordKeyStore;
        private String passwordPrivateKey = "testpassword";

        CloseableHttpClient client;

        public int closeNotification(Map<String, String> attributes) {

            String eventID = attributes.get(HPomiTicketUpdateHandler.EVENT_ID);
            int responseCode = 0;
            HttpPut post = null;
            String response;
            String requestXml = "<event_change>" + "<event_ref>" + "<event_id>" + eventID + "</event_id>" + "</event_ref>"
                    + "<changed_properties>" + "<state>closed</state>" + "</changed_properties>" + "</event_change>";

            SynchBackUrlDTO synchBackUrlDTO = null;
            try {
                synchBackUrlDTO = httpPostFactory.get(attributes.get(RadarTicketUpdateListener.SENDING_SERVER));
                if (synchBackUrlDTO != null) {
                    post = synchBackUrlDTO.getHttpPost();
                    post.setEntity(new StringEntity(requestXml));
                    post.setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_XML_VALUE);

                    UsernamePasswordCredentials creds = new UsernamePasswordCredentials(synchBackUrlDTO.getUsername(),
                            synchBackUrlDTO.getPassword());
                    post.addHeader(new BasicScheme().authenticate(creds, post, null));

                    client = getNewHttpsClient();

                    CloseableHttpResponse httpResponse = client.execute(post);
                    responseCode = httpResponse.getStatusLine().getStatusCode();
                    response = streamToStringAndCloseStream(httpResponse.getEntity().getContent(), "UTF-8");
                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        LOG.info("Received response from HP OMi: " + response);
                    } else {
                        LOG.info("Received error response from HP OMi: " + response);
                        LOG.info("Response Code : " + responseCode);
                    }
                } else {
                    LOG.info("Not a registered sending address :"
                            + attributes.get(RadarTicketUpdateListener.SENDING_SERVER));
                    responseCode = 1;
                }
            } catch (Exception e) {
                LOG.info("Exception communication with API at-- " + post.getURI() + ": " + e);
                response = null;
                responseCode = 0;
            } finally {
                if (post != null)
                    post.releaseConnection();
            }
            return responseCode;
        }

        public CloseableHttpClient getNewHttpsClient() throws Exception {

            SSLContext sslcontext = getNewSSLContext();
            SSLConnectionSocketFactory factory = new SSLConnectionSocketFactory(sslcontext,
                    SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
            client = HttpClients.custom().setSSLSocketFactory(factory).build();

            return (CloseableHttpClient) client;
        }

        private SSLContext getNewSSLContext() throws KeyStoreException, NoSuchAlgorithmException, CertificateException,
                IOException, KeyManagementException, UnrecoverableKeyException {
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());

            FileInputStream instream = new FileInputStream(new File(pathKeyStore));
            try {
                keyStore.load(instream, passwordKeyStore.toCharArray());
                LOG.info("Values of TrustStore" + keyStore.size() + "    " + keyStore.toString());
                LOG.info("Path of Keystore " + pathKeyStore);
            } finally {
                instream.close();
            }

            KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            keyManagerFactory.init(keyStore, passwordPrivateKey.toCharArray());

            TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            tmf.init(keyStore);

            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(keyManagerFactory.getKeyManagers(), tmf.getTrustManagers(), new java.security.SecureRandom());
            return sslContext;
        }

        private String streamToStringAndCloseStream(InputStream stream, String charSet) throws IOException {
            StringBuilder rbuf = new StringBuilder();
            char[] cbuf = new char[1024];
            Reader reader = new InputStreamReader(stream, charSet);
            int read;
            do {
                read = reader.read(cbuf, 0, cbuf.length);
                if (read > 0) {
                    rbuf.append(cbuf, 0, read);
                }
            } while (read >= 0);
            reader.close();
            stream.close();
            return rbuf.toString();
        }
    }
