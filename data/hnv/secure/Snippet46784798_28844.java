                try

                 {
                    KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
                    trustStore.load(null, null);
                    MySSLSocketFactory sf = new MySSLSocketFactory(trustStore);
                    sf.setHostnameVerifier(MySSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
                    sf.fixHttpsURLConnection();
                    HostnameVerifier hostnameVerifier = org.apache.http.conn.ssl.SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER;
                    HttpsURLConnection.setDefaultHostnameVerifier(hostnameVerifier);

                    mediaPlayer.setDataSource(MusicPlayerTestActivity.this, Uri.parse(urlFromServer)); // setup song from https://www.hrupin.com/wp-content/uploads/mp3/testsong_20_sec.mp3 URL to mediaplayer data source

                    mediaPlayer.prepare(); // you must call this method after setup the datasource in setDataSource method. After calling prepare() the instance of MediaPlayer starts load data from URL to internal buffer.
                    mediaPlayer.start();
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
