final WebView browser = new WebView();
            final WebEngine webEngine = browser.getEngine();
            webEngine.load("http://192.168.2.6:4200/");
            webEngine.setJavaScriptEnabled(true);
            browser.getEngine().getLoadWorker().stateProperty().addListener(new ChangeListener<Worker.State>() {
                public void changed(ObservableValue ov, Worker.State oldState, Worker.State newState) {
                    System.out.println(browser.getEngine().getLoadWorker().exceptionProperty());
                }
            });
            TrustManager trm = new X509TrustManager() {
                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }

                public void checkClientTrusted(X509Certificate[] certs, String authType) {

                }

                public void checkServerTrusted(X509Certificate[] certs, String authType) {
                }
            };

            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, new TrustManager[] { trm }, null);
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
