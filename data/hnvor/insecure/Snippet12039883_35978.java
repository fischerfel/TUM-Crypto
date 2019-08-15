URL url = new URL(httpsURL);

            HttpsURLConnection httpsCon = (HttpsURLConnection) url
                    .openConnection();

            httpsCon.setHostnameVerifier(new HostnameVerifier() {

                public boolean verify(String hostname, SSLSession session) {

                    return true;
                }
            });
