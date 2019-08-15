 SSLContext sslContext = SSLContext.getInstance("TLSv1");
                sslContext.init(null, null, null);
                SSLSocketFactory socketFactory = sslContext.getSocketFactory();
                            httpURLConnection.setSSLSocketFactory(socketFactory);
