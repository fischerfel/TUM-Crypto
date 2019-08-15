                    String myURL = "https://mysslservice";
                    SSLContext sslcontext = SSLContext.getInstance("TLS");
                    sslcontext.init(null, null, null);
                    SSLSocketFactory noSSLv3Factory = new NoSSLv3SocketFactory(sslcontext.getSocketFactory());
                    HttpsURLConnection.setDefaultSSLSocketFactory(noSSLv3Factory);

                    URL url = new URL(myURL);

                    HttpsURLConnection l_connection = (HttpsURLConnection) url.openConnection();
                    l_connection.connect();
