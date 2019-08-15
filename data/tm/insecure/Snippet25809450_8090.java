/*************************************************************************************************/
            /* Below code is only purposed for Testing, Not to use in real environment */
            /**
             * Setting custom Trust managers which are intended to allow SSL connection to server.
             * This custom trust managers are allowing for all connection types, so this may cause network connection security leak.
             * So those are used only for testing purposes.
             *              
             * Doc - http://developer.android.com/training/articles/security-ssl.html#SelfSigned
             * */
            WebSocketClient.setTrustManagers(new TrustManager[] {
              new X509TrustManager() {
                    public void checkClientTrusted(X509Certificate[] chain, String authType) {}
                    public void checkServerTrusted(X509Certificate[] chain, String authType) {}
                    public X509Certificate[] getAcceptedIssuers() { return new X509Certificate[]{}; }
                  }
            });
            /*************************************************************************************************/

            wsClient = new WebSocketClient(uri, this , extraHeaders);       
            wsClient.connect();
