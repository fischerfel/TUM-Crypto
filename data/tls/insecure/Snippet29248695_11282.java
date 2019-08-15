// Tell the URLConnection to use a SocketFactory from our SSLContext
                HttpsTransportSE transport = new HttpsTransportSE("nnn.nnn.nnn.nnnn", xxxx, "", 500000);
                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.setOutputSoapObject(IFXRq);
                transport.debug = true;
                envelope.setAddAdornments(false);
                envelope.implicitTypes = true;

                try {
                    AssetManager assetManager = getAssets();
                    InputStream caInput = assetManager.open("rootcert.der");
                    Certificate ca;
                    try {
                        CertificateFactory cf = CertificateFactory.getInstance("X.509", "BC");
                        ca = cf.generateCertificate(caInput);
                        //    System.out.println("ca=" + ((X509Certificate) ca).getSubjectDN());
                    } finally {
                        caInput.close();
                    }

                    // Create a KeyStore containing our trusted CAs
                    String keyStoreType = KeyStore.getDefaultType();
                    keyStore = KeyStore.getInstance(keyStoreType);
                    keyStore.load(null, null);
                    keyStore.setCertificateEntry("ca", ca);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }

                // Create a TrustManager that trusts the CAs in our KeyStore
                String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
                TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
                tmf.init(keyStore);

                // Create an SSLContext that uses our TrustManager
                SSLContext context = SSLContext.getInstance("SSL");
                context.init(null, tmf.getTrustManagers(), null);
                SSLSocketFactory ctx = context.getSocketFactory();

                try {
                    ((HttpsServiceConnectionSE) transport.getConnection()).setSSLSocketFactory(ctx);
                }/* catch (KeyManagementException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                } catch (KeyStoreException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                } catch (NoSuchAlgorithmException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }*/ catch (Exception e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
                transport.call(SOAP_ACTION, envelope);
