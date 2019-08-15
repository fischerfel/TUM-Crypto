TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());

        KeyStore keyStore = KeyStore.getInstance("BKS");
        InputStream in = context.getResources().openRawResource(de.thm.telemonitoring_2_0.R.raw.test2);
        try {
            keyStore.load(in, "1234".toCharArray());
        } catch (CertificateException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            in.close();
        }



        tmf.init(keyStore);
        SSLContext sslcontext = SSLContext.getInstance("SSL");
        sslcontext.init(null, tmf.getTrustManagers(), null);


            HttpsTransportSE transport = new HttpsTransportSE("SERVER",PORT,"FILE",TIMEOUT);
            ((HttpsServiceConnectionSE) transport.getServiceConnection()).setSSLSocketFactory(sslcontext.getSocketFactory());

            try {
                transport.call("", envelope);
                //response = envelope.getResponse();
                //Log.w("WEBSERVICE", "call erfolgreich" + String.valueOf(response));
            } catch (XmlPullParserException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            return null;
    }
