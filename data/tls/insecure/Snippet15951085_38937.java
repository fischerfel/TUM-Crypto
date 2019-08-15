private static SoapObject getBody(final SoapSerializationEnvelope soapEnvelope) throws Exception {
        if (soapEnvelope.bodyIn == null) {
            throw new Exception("soapEnvelope.bodyIn=null");
        }
        else if (soapEnvelope.bodyIn.getClass() == SoapFault.class) {
            throw new ExceptionLogic((SoapFault) soapEnvelope.bodyIn));
        }
        else {
            return (SoapObject) soapEnvelope.bodyIn;
        }

    }

private static SoapSerializationEnvelope sendRequete(final SoapObject soapReq, final String classMappingName,
            final Class<?> classMapping, final int timeOutSpecial) {



        final SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        soapEnvelope.implicitTypes = true;
        soapEnvelope.dotNet = true;

        if (classMappingName != null) {
            soapEnvelope.addMapping(NAMESPACE, classMappingName, classMapping);
        }

        soapEnvelope.setOutputSoapObject(soapReq);

        try {

            final HttpTransportSE httpTransport = new HttpTransportSE(Constante.urlWebService, timeOutSpecial);
            httpTransport.debug = BuildConfig.DEBUG;

            // Prod
            if (Constante.urlWebService.startsWith("https://")) {
                final List<HeaderProperty> headerList = new ArrayList<HeaderProperty>();
                headerList.add(new HeaderProperty("Authorization", "Basic "
                        + org.kobjects.base64.Base64.encode((Constante.CERTIFICAT_LOGIN + ":" + Constante.CERTIFICAT_MDP).getBytes())));

                FakeX509TrustManager.allowAllSSL();
                httpTransport.call(NAMESPACE + "/" + soapReq.getName(), soapEnvelope, headerList);
            }
            // Test
            else {
                httpTransport.call(NAMESPACE + "/" + soapReq.getName(), soapEnvelope);
            }

            return soapEnvelope;
        }
        catch (final Exception e) {
            throw new Exception("Erreur : " + e.getMessage(), e);
        }

    }



    private static class FakeX509TrustManager implements X509TrustManager {
        private static TrustManager[] trustManagers;
        private final X509Certificate[] _AcceptedIssuers = new X509Certificate[] {};

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return _AcceptedIssuers;
        }

        public static void allowAllSSL() {
            HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {

                @Override
                public boolean verify(final String hostname, final SSLSession session) {
                    return true;
                }
            });
            SSLContext context = null;
            if (trustManagers == null) {
                trustManagers = new TrustManager[] { new FakeX509TrustManager() };
            }
            try {
                context = SSLContext.getInstance("TLS");
                context.init(null, trustManagers, new SecureRandom());
            }
            catch (final NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
            catch (final KeyManagementException e) {
                e.printStackTrace();
            }
            HttpsURLConnection.setDefaultSSLSocketFactory(context.getSocketFactory());
        }

        @Override
        public void checkClientTrusted(final X509Certificate[] arg0, final String arg1) throws CertificateException {

        }

        @Override
        public void checkServerTrusted(final X509Certificate[] chain, final String authType) throws CertificateException {

        }
    }
