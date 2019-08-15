           KeyStore trustStore = KeyStore.getInstance("PKCS12");
            File key = new File ("/was85/resources/security/ecommerce_gr_mobile.p12");
            trustStore.load(new FileInputStream(key), "Pass".toCharArray());
            logger.info(">>>>>>>>>>>>>>>trustStore loaded <<<<<<<<<<" + String.valueOf(trustStore) );



            TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            tmf.init(trustStore);
            logger.info(">>>>>>>>>>>>>>>tmf init <<<<<<<<<<" + String.valueOf(tmf));
            TrustManager[] tms = tmf.getTrustManagers();
             logger.info(">>>>>>>>>>>>>>>tms init <<<<<<<<<<" + String.valueOf(tms));
            SSLContext sslContext = null;
            sslContext = SSLContext.getInstance("TLS");
             logger.info(">>>>>>>>>>>>>>>sslContext  <<<<<<<<<<");
            sslContext.init(null, tms, new SecureRandom());

            HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());
            HttpsURLConnection con = (HttpsURLConnection)url.openConnection();
            con.connect();
            logger.info("con     "+con);
            //con.setSSLSocketFactory(sslFactory);

            InputStream input = con.getInputStream(); 
            logger.info("input   " + input);

            byte[] bytes = org.apache.commons.io.IOUtils.toByteArray(input);
            logger.info("bytes   " + bytes);
            input.close();
