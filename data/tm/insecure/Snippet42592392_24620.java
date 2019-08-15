public List<String> doPostWithSSL(String direction, String dataToSend, String contentType, boolean OverrideSecurityVerifications) {
        try {
            URL url = new URL(direction);
            List<String> webcontent = new ArrayList();

            HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Host", getHostByUrl(direction));
            conn = new UserAgentsLibrary().getRandomUserAgent(conn);

            if (contentType != null) {
                conn.setRequestProperty("Content-Type", contentType);
            } else {
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            }

            conn.setDoOutput(true);

            if (OverrideSecurityVerifications) {
                try {
                    TrustManager[] trustAllCerts;
                    trustAllCerts = new TrustManager[]{new X509TrustManager() {
                        @Override
                        public X509Certificate[] getAcceptedIssuers() {
                            return new X509Certificate[0];
                        }

                        @Override
                        public void checkClientTrusted(X509Certificate[] certs, String authType) throws CertificateException {
                        }

                        @Override
                        public void checkServerTrusted(X509Certificate[] certs, String authType) throws CertificateException {
                        }

                    }};

                    // We want to override the SSL verifications
                    SSLContext ctx = SSLContext.getInstance("TLS");
                    ctx.init(null, trustAllCerts, null);
                    SSLSocketFactory factory = ctx.getSocketFactory();



                    conn.setDefaultSSLSocketFactory(ctx.getSocketFactory());
                    HostnameVerifier allHostsValid = (String hostname1, SSLSession session) -> true;
                    conn.setDefaultHostnameVerifier(allHostsValid);

                    conn.setSSLSocketFactory(factory);

                } catch (KeyManagementException kex) {
                    System.out.println("[+] Error bypassing SSL Security " + kex.getMessage());
                } catch (NoSuchAlgorithmException nsex) {
                    System.out.println("[+] Error forgeting TLS " + nsex.getMessage());
                }

            }

            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
            wr.write(dataToSend);
            wr.flush();

            BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;

            while ((line = rd.readLine()) != null) { //todo+=line+"\n";            
                webcontent.add(line);
            }

            wr.close();
            rd.close();
            return webcontent;

        } catch (MalformedURLException mex) {
            System.out.println("[+] Error: I received a malformed URL");
            return null;
        } catch (SSLHandshakeException sslex) {
            System.out.println("[+] Error: SSL Handshake Error!" + sslex.getMessage());
            return null;
        } catch (IOException ioex) {
            System.out.println("[+] Error: Input/Output Error!" + ioex.getMessage());
            return null;
        }
    }
