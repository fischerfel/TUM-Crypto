try {
            TrustManager[] wrappedTrustManagers = new TrustManager[]{
                    new X509TrustManager() {
                        public void checkClientTrusted(X509Certificate[] chain, String authType) {
                        }

                        public void checkServerTrusted(X509Certificate[] chain, String authType) {
                        }

                        public X509Certificate[] getAcceptedIssuers() {
                            return new X509Certificate[]{};
                        }
                    }
            };

            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, wrappedTrustManagers, null);

            AsyncSSLSocketMiddleware sslMiddleWare = Ion.getDefault(this).getHttpClient().getSSLSocketMiddleware();
            sslMiddleWare.setTrustManagers(wrappedTrustManagers);
            sslMiddleWare.setHostnameVerifier(new HostnameVerifier() {
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });
            sslMiddleWare.setSSLContext(sslContext);

            Ion.with(this)
                    .load("https://yoururl")
                    .setBodyParameter("key1", "value1")
                    .setBodyParameter("key2", "value2")
                    .asString()
                    .setCallback(new FutureCallback<String>() {
                        @Override
                        public void onCompleted(Exception e, String result) {
                            if (result != null)
                                Log.d("responsearrived", result);

                            if (e != null) Log.d("responserror", e.toString());
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
