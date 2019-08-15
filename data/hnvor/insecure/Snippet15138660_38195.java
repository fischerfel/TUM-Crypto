            InputStream inputStream = null;

            // is https protocol?
            if (url.getProtocol().toLowerCase().equals("https")) {

                trustAllHosts();
                // create connection
                HttpsURLConnection httpsUrlConnection = null;
                if(proxy != null){
                    httpsUrlConnection = (HttpsURLConnection) url.openConnection(proxy);
                } else {
                    httpsUrlConnection = (HttpsURLConnection) url.openConnection();
                }
                // set the check to: do not verify
                httpsUrlConnection.setHostnameVerifier(new HostnameVerifier() {
                    @Override
                    public boolean verify(String hostname, SSLSession session) {
                        return true;
                    }
                });

                setHeaders(httpsUrlConnection, headers);    

                //set del certificato

                log.debug("set certificate for get...");
                File cerp12 = new File(Utils.getWebAppLocalPath(),"WEB-INF"+String.valueOf(File.separatorChar)+PropConfig.getProperty("cer.p12"));
                ((HttpsURLConnection) httpsUrlConnection).setSSLSocketFactory(security(cerp12,PropConfig.getProperty("cer.pwd"))); 
                httpsUrlConnection.connect();

                inputStream = httpsUrlConnection.getInputStream();

            } else {
                HttpURLConnection httpUrlConnection = null;
                if(proxy != null){
                    httpUrlConnection = (HttpURLConnection) url.openConnection(proxy);
                } else {
                    httpUrlConnection = (HttpURLConnection) url.openConnection();
                }

                setHeaders(httpUrlConnection, headers);    

                inputStream = httpUrlConnection.getInputStream();
            }

            in = new BufferedReader(new InputStreamReader(inputStream));

            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                result.append(inputLine);
            }
