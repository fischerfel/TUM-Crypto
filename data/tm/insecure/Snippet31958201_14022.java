Add my response with cookie :

    public static void main(String[] args) {
            MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
            params.add("username", testUser);
            params.add("password", testPass);
NullHostnameVerifier verifier = new NullHostnameVerifier(); 
            MySimpleClientHttpRequestFactory requestFactory = new MySimpleClientHttpRequestFactory(verifier , rememberMeCookie);
            ResponseEntity<String> response = restTemplate.postForEntity(appUrl + "/login", params, String.class);

            HttpHeaders headers = response.getHeaders();
            String cookieResponse = headers.getFirst("Set-Cookie");
            String[] cookieParts = cookieResponse.split(";");
            rememberMeCookie = cookieParts[0];
            cookie.setCookie(rememberMeCookie);

            requestFactory = new  MySimpleClientHttpRequestFactory(verifier,cookie.getCookie());
            restTemplate.setRequestFactory(requestFactory);
    }


    public class MySimpleClientHttpRequestFactory extends SimpleClientHttpRequestFactory {

        private final HostnameVerifier verifier;
        private final String cookie;

        public MySimpleClientHttpRequestFactory(HostnameVerifier verifier ,String cookie) {
            this.verifier = verifier;
            this.cookie = cookie;
        }

        @Override
        protected void prepareConnection(HttpURLConnection connection, String httpMethod) throws IOException {
            if (connection instanceof HttpsURLConnection) {
                ((HttpsURLConnection) connection).setHostnameVerifier(verifier);
                ((HttpsURLConnection) connection).setSSLSocketFactory(trustSelfSignedSSL().getSocketFactory());
                ((HttpsURLConnection) connection).setAllowUserInteraction(true);
                String rememberMeCookie = cookie == null ? "" : cookie; 
                ((HttpsURLConnection) connection).setRequestProperty("Cookie", rememberMeCookie);
            }
            super.prepareConnection(connection, httpMethod);
        }

        public SSLContext trustSelfSignedSSL() {
            try {
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
                ctx.init(null, new TrustManager[] { tm }, null);
                SSLContext.setDefault(ctx);
                return ctx;
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return null;
        }

    }


    public class NullHostnameVerifier implements HostnameVerifier {
           public boolean verify(String hostname, SSLSession session) {
              return true;
           }
        }
